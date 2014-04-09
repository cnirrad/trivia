package trivia.messages;

import java.util.List;

import trivia.model.Answer;
import trivia.model.Question;

public class WaitMessage extends Message {

    private List<Answer> answers;
    
    private Question question;
    
    private int[] guesses = {0, 0, 0, 0};
    
    public WaitMessage(List<Answer> answers) {
        super("WAIT");
        this.answers = answers;
        if (answers.size() > 0) {
            this.question = answers.get(0).getQuestion();
        }
        calc();
    }
    
    public int[] getGuesses() {
        return guesses;
    }
    
    public Question getQuestion() {
        return question;
    }
    
    protected void calc() {
        for (Answer a : answers) {
            int idx = a.getAnswer().charAt(0) - 'A';
            guesses[idx]++;
        }
    }
}
