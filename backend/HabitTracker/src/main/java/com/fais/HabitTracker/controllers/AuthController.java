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

    // TODO: HTTP request body is also DB Document - this should be decoupled
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        /*
         TODO: you can play with more functional approach, e.g.:

        return userService.registerUser(user.getUsername(), user.getPassword())
                .map(value -> ok("Users registered successfully"))
                .orElseGet(() -> badRequest().body("Users not registered successfully"));

         */

        /*
        TODO:  in UserService you wrap an entity in Optional just to check if operation is successful.
        Isn't it a little overengineered?
         */
        Optional<User> registeredUser = userService.registerUser(user.getUsername(), user.getPassword());

        if (registeredUser.isPresent()) {
            return ResponseEntity.ok("Users registered successfully");
        } else {
            /*
            TODO: please check if other code doesn't fit better here.
            please read the excellent discussion here: https://stackoverflow.com/questions/3825990/http-response-code-for-post-when-resource-already-exists
             */
            return ResponseEntity.badRequest().body("Users not registered successfully");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDTO userRequestDTO) {
        boolean isValid = userService.validateUserLogin(userRequestDTO.username(), userRequestDTO.password());

        /*
        TODO: you can check ternary operator here
         */
        if (isValid) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
