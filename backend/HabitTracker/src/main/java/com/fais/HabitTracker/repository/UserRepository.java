package com.fais.HabitTracker.repository;

import com.fais.HabitTracker.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/*
TODO: a lot of unused methods
 */
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> getUserByUsername(String username);
    List<User> findAllByActiveTrueOrderByCreatedAtDesc();
    Optional<User> findByUsername(String username);
}
