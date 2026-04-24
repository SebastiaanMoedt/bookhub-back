package fr.eni.bookhub_back.user.controller;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.user.dto.AuthUserDto;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.user.bll.UserService;
import fr.eni.bookhub_back.user.dto.UserNonSensibleDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ServiceResponse<UserNonSensibleDto>> login(@RequestBody AuthUserDto user) {
        return this.userService.login(user.getUsername(), user.getPassword());
    }
//
//    @GetMapping("/me")
//    public ResponseEntity<ServiceResponse<AuthUserDto>> getProfile(@RequestBody User user) {
//        return this.userService.login(user.getUsername(), user.getPassword());
//    }

}