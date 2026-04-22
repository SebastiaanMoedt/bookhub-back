package fr.eni.bookhub_back.user.dao;

import fr.eni.bookhub_back.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserDao {
    List<User> findAll();
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Integer id);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsernameAndPassword(String username, String password);
    void deleteByUsername(String username);
    User save(User user);
}
