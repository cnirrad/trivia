package trivia.service;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import trivia.exception.GameAlreadyStartedException;
import trivia.messages.TriviaMessage;
import trivia.model.Game;
import trivia.model.Game.State;
import trivia.model.Question;
import trivia.repository.GameRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TriviaService {
    
    private static final Logger logger = LoggerFactory.getLogger(TriviaService.class);
    
    @Autowired
    private GameRepository gameRepository;
    
	@Autowired
	private SimpMessagingTemplate messaging;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Game game;
	
    private DateTime nextStateTime;

	public Game startGame() {
	    this.game = gameRepository.findOne(1L);
	    if (game == null) {
	        throw new IllegalStateException("No game found in database.");
	    }
	    
	    if (game.isStarted()) {
	        throw new GameAlreadyStartedException();
	    }
	    
	    game.setState(State.STARTING);
	    gameRepository.save(game);
	    return game;
	}
	
	@Scheduled(fixedRate=1000)
	public void runGame() {
	    logger.debug("Game tick");
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
	
	protected void goToNextState() {
	    switch(game.getState()) {
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
            nextStateTime = DateTime.now().plus(Period.seconds(game.getNumSecondsPerQuestion()));
        }
	}
	
	protected void presentWaitForQuestion() {
	    game.setState(State.WAIT);
	    broadCastGameState();
	    nextStateTime = DateTime.now().plus(Period.seconds(game.getNumSecondsBetweenQuestions()));
	}
	
    protected void broadCastGameState() {
        try {
            TriviaMessage m = TriviaMessage.fromGame(game);
            
            logger.debug("Broadcasting to /topic/trivia: " + m);
            
            messaging.convertAndSend("/topic/trivia",
                    mapper.writeValueAsString(m));
        } catch (MessagingException | JsonProcessingException e) {
            logger.warn("Exception on sending a TriviaMessage", e);
        }
    }
}
