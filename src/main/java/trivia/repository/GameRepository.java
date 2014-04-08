package trivia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import trivia.model.GameEntity;

@Repository
public interface GameRepository extends CrudRepository<GameEntity, Long> {

}
