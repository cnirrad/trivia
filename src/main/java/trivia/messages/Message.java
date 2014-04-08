package trivia.messages;

import trivia.model.Game;
import trivia.model.GameEntity.State;

public class Message {

    private String state;

    public Message(String type) {
        this.state = type;
    }

    public String getState() {
        return state;
    }

    /**
     * Factory method to create the appropriate message from the current game state.
     * 
     * @param g
     *            the game state
     * @return a message that will inform the clients everything they need to know about the new state.
     */
    public static Message fromGame(Game g) {
        if (g.getState() == State.QUESTION) {
            return new TriviaMessage(g.getCurrentQuestion());
        } else {
            return new Message(g.getState().toString());
        }
    }
}
