package fr.eni.bookhub_back.user.dao;

import fr.eni.bookhub_back.user.bo.User;

import java.util.Optional;

public interface IUserDao {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Integer userId);
    User save(User user);
}
