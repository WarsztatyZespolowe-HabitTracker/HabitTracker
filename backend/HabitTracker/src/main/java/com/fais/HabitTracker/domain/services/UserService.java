package com.fais.HabitTracker.domain.services;

import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.adapters.in.dto.register.UserResponseDTO;
import com.fais.HabitTracker.domain.models.User;

import java.util.List;

public interface UserService {
    User registerUser(String username, String password);

    boolean validateUserLogin(String username, String password);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(String id);

    UserResponseDTO createUser(UserRequestDTO request);

    UserResponseDTO updateUser(String id, UserRequestDTO request);

    void deleteUser(String id);
}
