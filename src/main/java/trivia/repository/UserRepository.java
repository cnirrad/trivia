package trivia.repository;

import org.springframework.data.repository.CrudRepository;

import trivia.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	User findByName(String name);
	
	User findByNameAndToken(String name, String token);
}
