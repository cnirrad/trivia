package trivia.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import trivia.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String name);

    User findByNameAndToken(String name, String token);
    
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.score = 0")
    public void resetAllScores();
    
    @Modifying
    @Transactional
    @Query("DELETE User u WHERE u.name != 'admin'")
    public void deleteAllExceptAdmin();
}
