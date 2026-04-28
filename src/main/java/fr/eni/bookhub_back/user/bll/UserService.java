package fr.eni.bookhub_back.user.bll;

import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.dao.jpa.UserJpaRepository;
import fr.eni.bookhub_back.user.dto.AuthUserDto;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.user.dao.IUserDao;
import fr.eni.bookhub_back.user.dto.UserNonSensibleDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private final IUserDao userDao;
    private final UserJpaRepository userJpaRepository;
    private final LocaleHelper lH;

    private UserNonSensibleDto getLimitedUserData(User user) {
        UserNonSensibleDto dto = new UserNonSensibleDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setRole(user.getRole());
        return dto;
    }

    public User findById(Integer userId) {
        Optional<User> user = userDao.findById(userId);
        return user.orElse(null);
    }

    public ResponseEntity<ServiceResponse<UserNonSensibleDto>> login(String username, String password) {
        Optional<User> userOpt = userDao.findByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ServiceResponse<>("USER_NOT_FOUND", "User not found", null));
        }
        User user = userOpt.get();
        //if (!passwordEncoder.matches(password, user.getPassword())) {
        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ServiceResponse<>("INVALID_CREDENTIALS", "Invalid credentials", null));
        }
        return ResponseEntity.ok(
                new ServiceResponse<>("LOGIN_SUCCESS", "Login success", getLimitedUserData(user))
        );
    }

    public ResponseEntity<ServiceResponse<User>> register(User user) {
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

    public ResponseEntity<ServiceResponse<List<User>>> findAllUsers() {
        try {
            List<User> users = userJpaRepository.findAll();

            ServiceResponse<List<User>> response =
                    new ServiceResponse<>("USERS_FOUND", lH.i18n("users.found"), users);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            ServiceResponse<List<User>> response =
                    new ServiceResponse<>("ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}