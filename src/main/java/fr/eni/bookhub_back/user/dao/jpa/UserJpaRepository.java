package fr.eni.bookhub_back.user.dao.jpa;

import fr.eni.bookhub_back.user.bo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    // --- inutile d'ajouter la méthode save(user) car Spring Data JPA s'occupe déjà de l'implémentation ---
}
