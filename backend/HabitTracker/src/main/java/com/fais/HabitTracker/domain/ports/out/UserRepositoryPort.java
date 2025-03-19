package com.fais.HabitTracker.domain.ports.out;

import com.fais.HabitTracker.domain.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserRepositoryPort {
    Optional<User> findUserByUsername(String username);
    User save(User user);
}
