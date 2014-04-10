package trivia.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import trivia.model.Answer;
import trivia.model.Question;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

    @Query("SELECT SUM(a) FROM Answer a WHERE a.question.id = ?1 AND a.answer = ?2")
    public Number getNumCorrect(long questionId, String correctAnswer);
    
    public List<Answer> findByQuestion(Question q);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Answer a WHERE a.user.id = ?1")
    public void deleteByUserId(long userId);
}
