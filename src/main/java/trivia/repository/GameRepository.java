package trivia.repository;

import org.springframework.data.repository.CrudRepository;

import trivia.model.Game;

public interface GameRepository extends CrudRepository<Game, Long> {

}
