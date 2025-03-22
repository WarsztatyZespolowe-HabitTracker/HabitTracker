package com.fais.HabitTracker.domain.services;

import com.fais.HabitTracker.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {
    Optional<User> registerUser(String username, String password);

    boolean validateUserLogin(String username, String password);
}
