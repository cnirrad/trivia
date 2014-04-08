package trivia.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * This is the database persistence object for the game.
 */
@Entity(name = "Game")
public class GameEntity {

    public enum State {
        NOT_STARTED, STARTING, QUESTION, WAIT, FINISH
    }

    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private State state;

    private int currentQuestionIdx;

    private int numSecondsPerQuestion;

    private int numSecondsBetweenQuestions;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Question> questions;

    public GameEntity() {
        state = State.NOT_STARTED;
    }

    public GameEntity(long id) {
        this();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setState(State started) {
        this.state = started;
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIdx >= 0 && currentQuestionIdx < questions.size()) {
            return questions.get(currentQuestionIdx);
        }
        return null;
    }

    public int getCurrentQuestionIdx() {
        return currentQuestionIdx;
    }

    public void setCurrentQuestionIdx(int currentQuestion) {
        this.currentQuestionIdx = currentQuestion;
    }

    public int getNumSecondsPerQuestion() {
        return numSecondsPerQuestion;
    }

    public void setNumSecondsPerQuestion(int numSecondsPerQuestion) {
        this.numSecondsPerQuestion = numSecondsPerQuestion;
    }

    public int getNumSecondsBetweenQuestions() {
        return numSecondsBetweenQuestions;
    }

    public void setNumSecondsBetweenQuestions(int numSecondsBetweenQuestions) {
        this.numSecondsBetweenQuestions = numSecondsBetweenQuestions;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question q) {
        if (questions == null) {
            questions = new ArrayList<Question>();
        }
        questions.add(q);
    }

}
