package trivia.service;

import java.io.IOException;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import trivia.exception.GameAlreadyStartedException;
import trivia.messages.GuessResponseMessage;
import trivia.messages.Message;
import trivia.model.Answer;
import trivia.model.Game;
import trivia.model.GameEntity;
import trivia.model.GameEntity.State;
import trivia.model.Question;
import trivia.model.User;
import trivia.repository.AnswerRepository;
import trivia.repository.GameRepository;
import trivia.repository.UserRepository;

/**
 * A service that keeps track of the current game.
 * 
 * runGame will be invoked every second to keep determine if the current state needs to be changed,
 * for example when the time limit for a question has been reached.
 * 
 * This keeps the Game state in memory, periodically saving it to the database. This is not very
 * scalable (or even thread-safe), but was done this way for simplicity.
 */
@Service
public class TriviaServiceImpl implements TriviaService {

    private static final Logger logger = LoggerFactory.getLogger(TriviaServiceImpl.class);

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messaging;

    @Autowired
    private SeedDataService seedData;

    @Autowired
    private TaskScheduler taskScheduler;

    /**
     * The game state. This will be written to the database at key points in the games lifecycle.
     */
    private Game game;

    /*
     * (non-Javadoc)
     * 
     * @see trivia.service.TriviaService#startGame()
     */
    @Override
    public Game startGame() {
        this.game = getGame();
        if (game == null) {
            throw new IllegalStateException("No game found in database.");
        }

        if (game.getQuestions() == null) {
            throw new IllegalStateException("No questions found in database.");
        }

        if (game.isStarted()) {
            throw new GameAlreadyStartedException();
        }

        game.setState(State.STARTING);
        goToNextState();
        return game;
    }

    /**
     * Based on the current state, transition to the next.
     */
    public void goToNextState() {
        switch (game.getState()) {
        case NOT_STARTED:
            break;
        case STARTING:
            presentNextQuestion();
            break;
        case QUESTION:
            presentWaitForQuestion();
            break;
        case WAIT:
            presentNextQuestion();
            break;
        case FINISH:
            break;
        }
    }

    /**
     * Gets the next question, and sends it to the trivia topic.
     */
    protected void presentNextQuestion() {
        Question q = game.nextQuestion();
        if (q == null) {
            // End of the game!
            game.setState(State.FINISH);
            broadCastGameState();
        } else {
            // Broadcast the question
            game.setState(State.QUESTION);
            broadCastGameState();
        }
        game.setQuestionAskedAt(DateTime.now());

        DateTime questionEnd = DateTime.now().plus(Period.seconds(game.getNumSecondsPerQuestion()));
        logger.debug("Time is up at: " + questionEnd.toString());

        taskScheduler.schedule(new Runnable() {

            @Override
            public void run() {
                logger.debug("Time is up!");
                goToNextState();
            }

        }, questionEnd.toDate());

        gameRepository.save(game.getEntity());
    }

    /**
     * Set the state to WAIT and broadcast to the users.
     */
    protected void presentWaitForQuestion() {
        game.setState(State.WAIT);

        List<Answer> answers = answerRepository.findByQuestion(game.getCurrentQuestion());
        game.setAnswers(answers);

        broadCastGameState();
        game.setNextStateTime(DateTime.now().plus(Period.seconds(game.getNumSecondsBetweenQuestions())));
        gameRepository.save(game.getEntity());
    }

    /**
     * Broadcast the current game to the users via the STOMP websocket.
     */
    protected void broadCastGameState() {
        try {
            Message m = Message.fromGame(game);

            logger.debug("Broadcasting to /topic/trivia: " + m);

            messaging.convertAndSend("/topic/trivia", m);
        } catch (Exception e) {
            logger.warn("Exception on sending a TriviaMessage", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see trivia.service.TriviaService#reset()
     */
    @Override
    public GameEntity reset() {
        if (game == null) {
            game = getGame();
        }
        game.setState(State.NOT_STARTED);
        game.setCurrentQuestionIdx(0);

        return gameRepository.save(game.getEntity());
    }

    /*
     * (non-Javadoc)
     * 
     * @see trivia.service.TriviaService#guess(trivia.model.User, java.lang.Long, java.lang.String)
     */
    @Override
    public GuessResponseMessage guess(User user, Long questionId, String answer) {
        if (game == null || game.getQuestionAskedAt() == null) {
            logger.warn("Users guessing when no question was asked or no game in progress: " + user);
            return new GuessResponseMessage(answer, 0, 0);
        }

        Interval guessInterval = new Interval(game.getQuestionAskedAt(), DateTime.now());
        Question q = game.getCurrentQuestion();

        if (q.getId() != questionId) {
            logger.warn(user.toString() + " is answering the wrong question!");
            return new GuessResponseMessage(answer, 0, 0);
        }
        Duration guessedIn = guessInterval.toDuration();

        // Did they guess in the allotted time?
        if (guessedIn.isLongerThan(Seconds.seconds(game.getNumSecondsPerQuestion()).toStandardDuration())) {
            logger.debug("Oops, " + user + " didn't answer within " + game.getNumSecondsPerQuestion() + "seconds.");
            return new GuessResponseMessage(answer, 0, 0);
        }

        GuessResponseMessage response = new GuessResponseMessage(answer, 0, guessedIn.getMillis());
        if (q.getCorrectAnswer().equalsIgnoreCase(answer)) {
            int score = score(guessedIn);
            user.addScore(score);
            userRepository.save(user);
            response.setPoints(score);
        }

        Answer a = new Answer(q, user, guessedIn.getMillis(), answer);
        answerRepository.save(a);

        return response;
    }

    private int score(Duration d) {
        // TODO: Determine how many points to assign based on how long it took them to answer.
        return 5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see trivia.service.TriviaService#getGame()
     */
    @Override
    public Game getGame() {
        if (game == null) {
            GameEntity e = gameRepository.findOne(1L);
            if (e == null) {
                try {
                    seedData.load();

                    e = gameRepository.findOne(1L);
                } catch (IOException e1) {
                    logger.error("Failed to load seed data.", e1);
                }
            }
            game = new Game(e);
        }

        return game;
    }

    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void setAnswerRepository(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messaging = messagingTemplate;
    }

    @Override
    public void updateGame(GameEntity game) {
        gameRepository.save(game);
        this.game.setEntity(game);
    }

}
