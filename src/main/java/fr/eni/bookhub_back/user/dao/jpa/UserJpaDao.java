package fr.eni.bookhub_back.user.dao.jpa;

import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.user.dao.IUserDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJpaDao implements IUserDao {

    private final UserJpaRepository userRepository;

    public UserJpaDao(UserJpaRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
