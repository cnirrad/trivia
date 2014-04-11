package trivia.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @ManyToOne
    private Question question;
    
    @ManyToOne(fetch=FetchType.LAZY)
    private User user;
    
    private long milliseconds;
    
    private String answer;
    
    private int points;
    
    public Answer() {
        
    }
    
    public Answer(Question q, User u, long ms, String answer, int points) {
        this.question = q;
        this.user = u;
        this.milliseconds = ms;
        this.answer = answer;
        this.points = points;
    }

    public long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public User getUser() {
        return user;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public String getAnswer() {
        return answer;
    }
    
    public int getPoints() {
        return points;
    }
}
