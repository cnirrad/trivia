package trivia.service;

import trivia.messages.GuessResponseMessage;
import trivia.model.Game;
import trivia.model.GameEntity;
import trivia.model.User;

public interface TriviaService {

    /**
     * Starts the game (STATE -> STARTING) and updates the game record in the database.
     * 
     * @return the game object.
     * @throws IllegalStateException
     *             if a game doesn't exist in the database or no questions exist in the database.
     * @throws GameAlreadyExistsException
     *             if the game is already started.
     */
    public abstract Game startGame();

    /**
     * Reset the game state to NOT_STARTED and the current question # to 0. Writes the
     * game object to the database.
     * 
     * @return
     */
    public abstract GameEntity reset();

    /**
     * Returns the game object.
     * 
     * @return the game state.
     */
    public abstract Game getGame();

    /**
     * Record a users guess to the question. Awards points to the user if the guess was correct.
     * 
     * @param user
     *            the user that made the guess.
     * @param questionId
     *            the ID of the question. This should match the current question ID of the game, otherwise the guess
     *            will not be recorded.
     * @param answer
     *            the users guess to the question.
     */
    public abstract GuessResponseMessage guess(User user, Long questionId, String answer);

    /**
     * Updates the game on the database and in memory.
     * 
     * @param game
     *            the new game object
     */
    public abstract void updateGame(GameEntity game);

}
