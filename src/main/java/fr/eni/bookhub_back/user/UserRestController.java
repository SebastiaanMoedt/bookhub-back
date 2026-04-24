package fr.eni.bookhub_back.user;
import fr.eni.bookhub_back.common.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// POST /api/auth/register, POST /api/auth/login
// GET /api/profile

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ServiceResponse<User>> register(@Valid @RequestBody User user) {
        return this.userService.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse<User>> login(@Valid @RequestBody User user) {
        return this.userService.login(user.getUsername(), user.getPassword());
    }

}