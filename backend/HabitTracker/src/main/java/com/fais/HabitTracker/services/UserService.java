package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> registerUser(String username, String password);

    boolean validateUserLogin(String username, String password);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO updateUser(String id, UserRequestDTO request);

    void deleteUser(String id);
}
