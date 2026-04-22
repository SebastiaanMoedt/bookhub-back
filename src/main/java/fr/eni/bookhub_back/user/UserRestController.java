package fr.eni.bookhub_back.user;
import fr.eni.bookhub_back.common.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// POST /api/auth/register, POST /api/auth/login
// GET /api/profile

@CrossOrigin
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity<ServiceResponse<User>> register(@Valid @RequestBody User user) {
        try {
            userService.save(user);
            ServiceResponse<User> response =
                    new ServiceResponse<>("USER_CREATED", "{user.created}", user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e){
            ServiceResponse<User> response =
                    new ServiceResponse<>("ERROR", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

//    @PostMapping("/api/auth/login")
//    public ResponseEntity<ServiceResponse<User>> login(@Valid @RequestBody User user) {
//        try {
//            userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//            return ResponseEntity.ok(user);
//        } catch (RuntimeException e){
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
//        }
//    }

//    @GetMapping("/api/users")
//    public ResponseEntity<List<User>> findAll() {
//        try {
//            List<User> users = userService.findAll();
//            if (users != null && !users.isEmpty()) {
//                ServiceResponse<List<User>> response =
//                        new ServiceResponse<>("USERS_FOUND", "{users.found}", users);
//                return ResponseEntity.ok(response);
//            }
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException e) {
//            ServiceResponse<User> response =
//                    new ServiceResponse<>("ERROR", e.getMessage());
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//    }

    @GetMapping("/api/users/{id}")
        public ResponseEntity<ServiceResponse<User>> findById(@PathVariable Integer id) {
            return userService.findById(id);
        }

    }