package fr.eni.bookhub_back.user;

import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.dao.IUserDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final IUserDao userDao;
    private final LocaleHelper lH;

    UserService(IUserDao userDao, LocaleHelper lH) {
        this.userDao = userDao;
        this.lH = lH;
    }

    public ResponseEntity<ServiceResponse<User>> findById(Integer id) {
        try{
            User user = userDao.findById(id).get();
            ServiceResponse<User> response =
                    new ServiceResponse<User>("USER_FOUND", lH.i18n("user.found"), user);
            return ResponseEntity.ok(response);
        } catch (NumberFormatException e){
            ServiceResponse<User> response =
                    new ServiceResponse<User>("USER_ID_FORMAT_ERROR", lH.i18n("user.id.format-error"), null);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        } catch (RuntimeException e) {
            ServiceResponse<User> response =
                    new ServiceResponse<User>("ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }


    public Optional<User> findByUsernameOrEmail(String username, String email) {
        return userDao.findByUsernameOrEmail(username, email);
    }

    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return userDao.findByUsernameAndPassword(username, password);
    }

    public void deleteByUsername(String username) {
        userDao.deleteByUsername(username);
    }

    public User save(User user) {
        return userDao.save(user);
    }
}
