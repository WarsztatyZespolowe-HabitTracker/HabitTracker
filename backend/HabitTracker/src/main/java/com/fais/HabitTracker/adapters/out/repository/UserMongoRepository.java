package com.fais.HabitTracker.adapters.out.repository;

import com.fais.HabitTracker.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserMongoRepository  extends MongoRepository<User, String> {
    Optional<User> getUserByUsername(String username);
    List<User> findAllByActiveTrueOrderByCreatedAtDesc();
}
