package trivia.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Game {
    
    public enum State {
        NOT_STARTED,
        STARTING,
        QUESTION,
        WAIT,
        FINISH
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

    public Game() {
        state = State.NOT_STARTED;
    }
    
    public Game(long id) {
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

    public boolean isStarted() {
        return state != State.NOT_STARTED;
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

    /**
     * Increments the current question and returns the next
     * question in the list. If at the end of the list, a 
     * null will be returned.
     * 
     * @return
     */
    public Question nextQuestion() {
        if (currentQuestionIdx == questions.size() - 1) {
            // At the end of the questions
            return null;
        }
        
        setCurrentQuestionIdx(++currentQuestionIdx);
        
        return questions.get(currentQuestionIdx);
    }
}
