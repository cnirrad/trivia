package trivia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    
    @Column(nullable=false)
    private String text;
    
    @Column(nullable=false)
    private String optionA;
    
    @Column(nullable=false)
    private String optionB;
    
    private String optionC;
    
    private String optionD;
    
    @Column(length=1, nullable=false)
    private String correctAnswer;
    
    @Override
    public String toString() {
        return "Question[#" + id + ":" + text + 
                ", A)" + optionA +
                ", B)" + optionA +
                ", C)" + optionA +
                ", D)" + optionA + ",*" + correctAnswer;
    }
}
