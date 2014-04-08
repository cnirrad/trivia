package trivia.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import trivia.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String name);

    User findByNameAndToken(String name, String token);
}
