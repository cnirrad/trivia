package trivia.test;

import trivia.model.GameEntity;
import trivia.model.GameEntity.State;
import trivia.model.Question;
import trivia.model.User;

public class DataFactory {

    public static GameEntity createGame() {
        GameEntity g = new GameEntity(1L);
        g.setState(State.NOT_STARTED);
        g.setNumSecondsBetweenQuestions(10);
        g.setNumSecondsPerQuestion(10);
        return g;
    }

    public static GameEntity createGameWithQuestions() {
        GameEntity g = createGame();

        for (int i = 0; i < 10; ++i) {
            g.addQuestion(createQuestion(i + 1));
        }
        return g;
    }

    public static Question createQuestion(long id) {
        Question q = new Question();
        q.setId(id);
        q.setText("Question #" + id);
        q.setOptionA("Option A");
        q.setOptionB("Option B");
        q.setOptionC("Option C");
        q.setOptionD("Option D");
        q.setCorrectAnswer("C");

        return q;
    }

    public static User createUser() {
        User u = new User();
        u.setAdmin(false);
        u.setName("junit");
        u.setScore(0);
        u.setToken("junit_token");
        return u;
    }
}
