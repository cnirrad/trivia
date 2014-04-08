package trivia.messages;

import trivia.model.Question;

/**
 * A {@link Message} that will present a new question to the users.
 */
public class TriviaMessage extends Message {

    private Question question;

    public TriviaMessage(Question q) {
        super("QUESTION");
        question = q;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "TriviaMessage[question=" + question + "]";
    }
}
