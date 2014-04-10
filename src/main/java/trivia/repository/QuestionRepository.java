package trivia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import trivia.model.Question;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long> {

}
