package trivia.messages;

import java.util.List;
import trivia.model.Answer;
import trivia.model.Question;

public class WaitMessage extends Message {

    private List<Answer> answers;

    private Question question;

    private int[] guesses = {0, 0, 0, 0};

    public WaitMessage(Question q, List<Answer> answers) {
        super("WAIT");
        this.answers = answers;
        this.question = q;
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
