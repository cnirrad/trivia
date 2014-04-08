package trivia.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import trivia.exception.GameAlreadyStartedException;
import trivia.model.Answer;
import trivia.model.GameEntity;
import trivia.model.GameEntity.State;
import trivia.model.User;
import trivia.repository.AnswerRepository;
import trivia.repository.GameRepository;
import trivia.repository.UserRepository;
import trivia.test.DataFactory;

/**
 * Test class for the {@link TriviaServiceImpl}.
 */
public class TriviaServiceImplTest {

    private TriviaServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        service = new TriviaServiceImpl();

        service.setAnswerRepository(answerRepository);
        service.setGameRepository(gameRepository);
        service.setUserRepository(userRepository);
        service.setMessagingTemplate(messagingTemplate);

    }

    @Test
    public void testStart() {
        GameEntity g = DataFactory.createGameWithQuestions();
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        service.startGame();

        assertEquals("State should be STARTING.", State.STARTING, g.getState());
        verify(gameRepository).save(eq(g));
    }

    @Test(expected = GameAlreadyStartedException.class)
    public void testStartWhenAlreadyStarted() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setState(State.QUESTION);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        service.startGame();
    }

    @Test(expected = IllegalStateException.class)
    public void testStartWithoutQuestions() {
        GameEntity g = DataFactory.createGame();
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        service.startGame();
    }

    @Test
    public void testReset() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setCurrentQuestionIdx(2);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        service.reset();

        assertEquals("State should be NOT_STARTED.", State.NOT_STARTED, g.getState());
        assertEquals("Current question should be the first question (ID=1).", 1, g.getCurrentQuestion().getId());
        verify(gameRepository).save(eq(g));
    }

    @Test
    public void testPresentNextQuestion() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setState(State.WAIT);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        // calling getGame only so that the internal state gets initialized
        service.getGame();

        service.presentNextQuestion();

        assertEquals("State should be QUESTION.", State.QUESTION, g.getState());
        assertNotNull("Question asked at time should not be null.", service.getGame().getQuestionAskedAt());
        assertNotNull("Next state time should not be null.", service.getGame().getNextStateTime());

        verify(messagingTemplate).convertAndSend(eq("/topic/trivia"), any());
        verify(gameRepository).save(eq(g));
    }

    @Test
    public void testPresentWaitForQuestion() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setState(State.QUESTION);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        // calling getGame only so that the internal state gets initialized
        service.getGame();

        service.presentWaitForQuestion();

        assertEquals("State should be WAIT.", State.WAIT, g.getState());
        assertNotNull("Next state time should not be null.", service.getGame().getNextStateTime());

        verify(messagingTemplate).convertAndSend(eq("/topic/trivia"), any());
        verify(gameRepository).save(eq(g));
    }

    @Test
    public void testGuessCorrect() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setState(State.WAIT);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        User u = DataFactory.createUser();

        // calling getGame only so that the internal state gets initialized
        service.getGame();

        // Present the question
        service.presentNextQuestion();

        service.guess(u, g.getCurrentQuestion().getId(), g.getCurrentQuestion().getCorrectAnswer());

        assertThat((Long) u.getScore(), greaterThan(0L));

        // Users score should have been updated.
        verify(userRepository).save(eq(u));

        // Users answer should have been recorded.
        verify(answerRepository).save(any(Answer.class));
    }

    @Test
    public void testGuessInCorrect() {
        GameEntity g = DataFactory.createGameWithQuestions();
        g.setState(State.WAIT);
        when(gameRepository.findOne(anyLong())).thenReturn(g);

        User u = DataFactory.createUser();

        // calling getGame only so that the internal state gets initialized
        service.getGame();

        // Present the question
        service.presentNextQuestion();

        service.guess(u, g.getCurrentQuestion().getId(), "X");

        assertEquals("Score should be 0.", 0, u.getScore());

        // User did not score any points, and so should not have been updated.
        verify(userRepository, never()).save(eq(u));

        // The answer is saved, even though it was incorrect.
        verify(answerRepository).save(any(Answer.class));
    }
}
