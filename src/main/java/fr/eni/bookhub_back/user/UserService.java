package fr.eni.bookhub_back.user;

import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.dao.IUserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final IUserDao userDao;
    private final LocaleHelper lH;
    private final PasswordEncoder passwordEncoder;

    UserService(IUserDao userDao, LocaleHelper lH, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.lH = lH;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<ServiceResponse<User>> login(String username, String password) {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServiceResponse<>("USER_NOT_FOUND", "User not found", null));
        }
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ServiceResponse<>("INVALID_CREDENTIALS", "Invalid credentials", null));
        }
        return ResponseEntity.ok(
                new ServiceResponse<>("LOGIN_SUCCESS", "Login success", user)
        );
    }


    public ResponseEntity<ServiceResponse<User>> save(User user) {
        try {
            userDao.save(user);
            ServiceResponse<User> response =
                    new ServiceResponse<>("USER_CREATED", "{user.created}", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            ServiceResponse<User> response =
                    new ServiceResponse<>("ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}