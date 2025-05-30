package com.fais.HabitTracker.controllers;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/edituser")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String username, @RequestBody UserRequestDTO request) {
        UserResponseDTO updatedUser = userService.updateUserByName(username, request);
        return ResponseEntity.ok(updatedUser);
    }

}
