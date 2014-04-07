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
    
    
    public long getId() {
        return id;
    }



    public void setId(long id) {
        this.id = id;
    }



    public String getText() {
        return text;
    }



    public void setText(String text) {
        this.text = text;
    }



    public String getOptionA() {
        return optionA;
    }



    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }



    public String getOptionB() {
        return optionB;
    }



    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }



    public String getOptionC() {
        return optionC;
    }



    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }



    public String getOptionD() {
        return optionD;
    }



    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }



    public String getCorrectAnswer() {
        return correctAnswer;
    }



    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }



    @Override
    public String toString() {
        return "Question[#" + id + ":" + text + 
                ", A)" + optionA +
                ", B)" + optionA +
                ", C)" + optionA +
                ", D)" + optionA + ",*" + correctAnswer;
    }
}
