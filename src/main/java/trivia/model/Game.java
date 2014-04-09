package trivia.model;

import java.util.List;
import org.joda.time.DateTime;
import trivia.model.GameEntity.State;

public class Game {

    /**
     * The game state that is persisted to the database.
     */
    private GameEntity entity;

    /**
     * The time at which the game should transition to the next state.
     */
    private DateTime nextStateTime;

    /**
     * The time at which the current question was broadcast to the users. This is used to figure
     * out how fast a user answered the question, which is then used to determine how many points
     * to award for a correct answer.
     */
    private DateTime questionAskedAt;
    
    /**
     * A list of answers given for the most recent question.
     */
    private List<Answer> answers;
    
    public Game(GameEntity entity) {
        this.entity = entity;
    }

    public GameEntity getEntity() {
        return entity;
    }

    public void setEntity(GameEntity entity) {
        this.entity = entity;
    }

    public DateTime getNextStateTime() {
        return nextStateTime;
    }

    public void setNextStateTime(DateTime nextStateTime) {
        this.nextStateTime = nextStateTime;
    }

    public DateTime getQuestionAskedAt() {
        return questionAskedAt;
    }

    public void setQuestionAskedAt(DateTime questionAskedAt) {
        this.questionAskedAt = questionAskedAt;
    }

    public List<Question> getQuestions() {
        return entity.getQuestions();
    }

    public boolean isStarted() {
        return entity.getState() != State.NOT_STARTED;
    }

    public void setState(State state) {
        entity.setState(state);
    }

    public State getState() {
        return entity.getState();
    }

    /**
     * Increments the current question and returns the next
     * question in the list. If at the end of the list, a
     * null will be returned.
     * 
     * @return
     */
    public Question nextQuestion() {
        int currentQuestionIdx = entity.getCurrentQuestionIdx();
        if (currentQuestionIdx == getQuestions().size() - 1) {
            // At the end of the questions
            return null;
        }

        setCurrentQuestionIdx(++currentQuestionIdx);

        return getQuestions().get(currentQuestionIdx);
    }

    public void setCurrentQuestionIdx(int i) {
        entity.setCurrentQuestionIdx(i);
    }

    public int getNumSecondsPerQuestion() {
        return entity.getNumSecondsPerQuestion();
    }

    public int getNumSecondsBetweenQuestions() {
        return entity.getNumSecondsBetweenQuestions();
    }

    public Question getCurrentQuestion() {
        return entity.getCurrentQuestion();
    }
    
    public List<Answer> getAnswers() {
        return answers;
    }
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

}
