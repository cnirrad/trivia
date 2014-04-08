package trivia.service;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import trivia.exception.GameAlreadyStartedException;
import trivia.messages.TriviaMessage;
import trivia.model.Answer;
import trivia.model.Game;
import trivia.model.Game.State;
import trivia.model.Question;
import trivia.model.User;
import trivia.repository.AnswerRepository;
import trivia.repository.GameRepository;
import trivia.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A service that keeps track of the current game.
 * 
 * runGame will be invoked every second to keep determine if the current state needs to be changed,
 * for example when the time limit for a question has been reached.
 * 
 * This keeps the Game state in memory, periodically saving it to the database. This is not very
 * scalable, but was done this way for simplicity.
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

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * The game state. This will be written to the database at key points in the games lifecycle.
     */
    private Game game;

    /**
     * The time at which the game should transition to the next state.
     */
    private DateTime nextStateTime;

    /**
     * The time at which the current question was broadcast to the users. This is used to figure
     * out how fast a user answered the question, which is then used to determine how many points
     * to award for a correct answer.
     */
    private DateTime questionAskedAt;

    /* (non-Javadoc)
     * @see trivia.service.TriviaService#startGame()
     */
    @Override
    public Game startGame() {
        this.game = gameRepository.findOne(1L);
        if (game == null) {
            throw new IllegalStateException("No game found in database.");
        }

        if (game.getQuestions() == null) {
            throw new IllegalStateException("No questions found.");
        }

        if (game.isStarted()) {
            throw new GameAlreadyStartedException();
        }

        game.setState(State.STARTING);
        gameRepository.save(game);
        return game;
    }

    /**
     * Manages the game. This will be invoked at a fixed rate to handle state transitions.
     */
    @Scheduled(fixedRate = 1000)
    protected void runGame() {
        if (game == null || !game.isStarted()) {
            // Game is not in progress
            return;
        }

        if (nextStateTime == null) {
            // Better present a question, eh?
            presentNextQuestion();
        } else {

            if (DateTime.now().isAfter(nextStateTime)) {
                goToNextState();
            }
        }
    }

    /**
     * Based on the current state, transition to the next.
     */
    protected void goToNextState() {
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
            // game.setState(State.FINISH);
            game.setCurrentQuestionIdx(0);
            game.setState(State.QUESTION);
            broadCastGameState();
            nextStateTime = DateTime.now().plus(Period.seconds(game.getNumSecondsPerQuestion()));
        } else {
            // Broadcast the question
            game.setState(State.QUESTION);
            broadCastGameState();
            nextStateTime = DateTime.now().plus(Period.seconds(game.getNumSecondsPerQuestion()));
        }
        questionAskedAt = DateTime.now();

        gameRepository.save(game);
    }

    /**
     * Set the state to WAIT and broadcast to the users.
     */
    protected void presentWaitForQuestion() {
        game.setState(State.WAIT);
        broadCastGameState();
        nextStateTime = DateTime.now().plus(Period.seconds(game.getNumSecondsBetweenQuestions()));
    }

    /**
     * Broadcast the current game to the users via the STOMP websocket.
     */
    protected void broadCastGameState() {
        try {
            TriviaMessage m = TriviaMessage.fromGame(game);

            logger.debug("Broadcasting to /topic/trivia: " + m);

            messaging.convertAndSend("/topic/trivia", mapper.writeValueAsString(m));
        } catch (Exception e) {
            logger.warn("Exception on sending a TriviaMessage", e);
        }
    }

    /* (non-Javadoc)
     * @see trivia.service.TriviaService#reset()
     */
    @Override
    public Game reset() {
        if (game == null) {
            game = gameRepository.findOne(1L);
        }
        game.setState(State.NOT_STARTED);
        game.setCurrentQuestionIdx(0);

        return gameRepository.save(game);
    }

    /* (non-Javadoc)
     * @see trivia.service.TriviaService#getGame()
     */
    @Override
    public Game getGame() {
        if (game == null) {
            game = gameRepository.findOne(1L);
        }

        return game;
    }

    /* (non-Javadoc)
     * @see trivia.service.TriviaService#guess(trivia.model.User, java.lang.Long, java.lang.String)
     */
    @Override
    public void guess(User user, Long questionId, String answer) {
        Interval guessInterval = new Interval(questionAskedAt, DateTime.now());
        Question q = game.getCurrentQuestion();

        if (q.getId() != questionId) {
            return;
        }
        Duration guessedIn = guessInterval.toDuration();

        if (q.getCorrectAnswer().equals(answer)) {
            int score = score(guessedIn);
            user.addScore(score);
            userRepository.save(user);
        }

        Answer a = new Answer(q, user, guessedIn.getMillis(), answer);
        answerRepository.save(a);
    }

    private int score(Duration d) {
        return 5;
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

}
