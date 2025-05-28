package com.fais.HabitTracker.controllers;


import com.fais.HabitTracker.constants.RestApi;
import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.models.user.User;
import com.fais.HabitTracker.services.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRequestDTO user) {
        User createdUser = userService.registerUser(user.username(), user.password());

        return createdUser != null ? ResponseEntity.ok("Users registered successfully")
                : ResponseEntity.status(HttpStatus.CONFLICT).body("Users not registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) {
        boolean isValid = userService.validateUserLogin(userRequestDTO.username(), userRequestDTO.password());

        return isValid ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
