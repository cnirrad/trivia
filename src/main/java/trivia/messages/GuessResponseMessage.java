package trivia.messages;

/**
 * A message that is sent in response to a guess. This will
 * tell the client how many points they got for that question.
 */
public class GuessResponseMessage extends Message {

    private int points;
    
    private long responseTime;
    
    private String guess;
    
    public GuessResponseMessage(String guess, int points, long responseTime) {
        super("GUESS");
        this.guess = guess;
        this.points = points;
        this.responseTime = responseTime;
    }
    
    public int getPoints() {
        return points;
    }
    
    public long getResponseTime() {
        return responseTime;
    }
    
    public void setPoints(int points) {
        this.points = points;
    }
    
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }
    
    public String getGuess() {
        return guess;
    }
    
    public void setGuess(String guess) {
        this.guess = guess;
    }
}
