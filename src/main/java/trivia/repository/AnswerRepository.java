package trivia.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import trivia.model.Answer;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {

}
