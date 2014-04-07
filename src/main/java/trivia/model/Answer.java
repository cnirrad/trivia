package trivia.model;

import javax.persistence.Entity;
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
    
    @ManyToOne
    private User user;
    
    private long milliseconds;
    
    private String answer;
    
    public Answer(Question q, User u, long ms, String answer) {
        this.question = q;
        this.user = u;
        this.milliseconds = ms;
        this.answer = answer;
    }
}
