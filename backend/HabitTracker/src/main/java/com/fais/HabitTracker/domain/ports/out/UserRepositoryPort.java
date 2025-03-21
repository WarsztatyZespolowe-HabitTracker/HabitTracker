package com.fais.HabitTracker.domain.ports.out;

import com.fais.HabitTracker.domain.models.User;

import java.util.List;
import java.util.Optional;


public interface UserRepositoryPort {
    Optional<User> findUserByUsername(String username);
    User save(User user);
    List<User> findAll();
    Optional<User> findById(String id);
}
