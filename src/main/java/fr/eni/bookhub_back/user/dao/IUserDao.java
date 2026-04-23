package fr.eni.bookhub_back.user.dao;

import fr.eni.bookhub_back.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserDao {
    Optional<User> findByUsername(String username);
    User save(User user);
}
