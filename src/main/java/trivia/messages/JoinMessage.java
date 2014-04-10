package trivia.messages;

import trivia.model.User;

public class JoinMessage extends Message {

    private User user;

    public JoinMessage(User user) {
        super("JOINED");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
