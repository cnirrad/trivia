package trivia.messages;

import trivia.model.Game;
import trivia.model.Question;

public class TriviaMessage {

    private Game.State state;
    
    private Question question;
    
    public TriviaMessage() {
        
    }
    
    public static TriviaMessage fromGame(Game g) {
        TriviaMessage m = new TriviaMessage();
        m.question = g.getCurrentQuestion();
        m.state = g.getState();
        return m;
    }

    public Game.State getState() {
        return state;
    }

    public void setState(Game.State state) {
        this.state = state;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    
    @Override
    public String toString() {
        return "TriviaMessage[state=" + state + ",question=" + question + "]";
    }
}
