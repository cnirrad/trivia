package trivia.messages;

public class GuessRequestMessage {

    private long questionNumber;
    
    private String guess;
    

    public void setQuestionNumber(long questionNumber) {
        this.questionNumber = questionNumber;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public long getQuestionNumber() {
        return questionNumber;
    }

    public String getGuess() {
        return guess;
    }
    
    
}
