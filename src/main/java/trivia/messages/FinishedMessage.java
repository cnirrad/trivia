package trivia.messages;

import java.util.List;

import trivia.model.User;

public class FinishedMessage extends Message {

    private List<User> users;
    
    public FinishedMessage(List<User> users) {
        super("FINISH");
        this.users = users;
    }
    
    public List<User> getUsers() {
        return users;
    }

}
