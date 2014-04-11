package trivia.messages;

import java.util.List;
import trivia.model.Answer;
import trivia.model.Question;

/**
 * The message sent to the STOMP clients when the time allotted to
 * answer the current question has expired. 
 *
 * This contains information about the guesses that have been
 * recieved as well as the original question.
 */
public class TimesUpMessage extends Message {

    private List<Answer> answers;

    private Question question;

    private int[] guesses = {0, 0, 0, 0};

    public TimesUpMessage(Question q, List<Answer> answers) {
        super("TIME_UP");
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
