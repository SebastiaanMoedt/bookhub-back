package fr.eni.bookhub_back.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    List<User> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsernameAndPassword(String username, String password);
    void deleteByUsername(String username);
    User save(User user);

}
