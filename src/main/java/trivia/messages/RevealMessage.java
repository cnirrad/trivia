package trivia.messages;

import java.util.ArrayList;
import java.util.List;

import trivia.model.Answer;


public class RevealMessage extends Message {

    static class UserAnswer {
        private String name;
        private long questionScore;
        private long overallScore;
        
        public UserAnswer(String name, long qScore, long oScore) {
            this.name = name;
            this.questionScore = qScore;
            this.overallScore = oScore;
        }
        
        public String getName() {
            return name;
        }
        public long getQuestionScore() {
            return questionScore;
        }
        public long getOverallScore() {
            return overallScore;
        }
        
        
    }
    
    private List<UserAnswer> users;
    
    public RevealMessage(List<Answer> answers) {
        super("REVEAL");
        users = new ArrayList<UserAnswer>(answers.size());
        
        for (Answer a : answers) {
            UserAnswer ua = new UserAnswer(a.getUser().getName(), a.getPoints(), a.getUser().getScore());
            users.add(ua);
        }
    }

    public List<UserAnswer> getUsers() {
        return users;
    }
}
