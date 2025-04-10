package com.fais.HabitTracker.controllers;


import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.constants.RestApi;
import com.fais.HabitTracker.models.User;
import com.fais.HabitTracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(RestApi.AUTH_API)
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        Optional<User> registeredUser = userService.registerUser(user.getUsername(), user.getPassword());

        if (registeredUser.isPresent()) {
            return ResponseEntity.ok("Users registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Users not registered successfully");
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
