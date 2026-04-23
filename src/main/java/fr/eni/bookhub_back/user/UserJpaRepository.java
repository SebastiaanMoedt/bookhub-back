package fr.eni.bookhub_back.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    User save(User user);
}
