package com.fais.HabitTracker.adapters.in;


import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.constants.RestApi;
import com.fais.HabitTracker.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestApi.AUTH_API)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
        try{
            userService.registerUser(userRequestDTO.username(), userRequestDTO.password());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) {
        boolean isValid = userService.validateUserLogin(userRequestDTO.username(), userRequestDTO.password());

        if (isValid) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
