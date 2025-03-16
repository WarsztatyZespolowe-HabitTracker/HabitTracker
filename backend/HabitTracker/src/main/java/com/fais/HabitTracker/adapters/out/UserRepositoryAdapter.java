package com.fais.HabitTracker.adapters.out;

import com.fais.HabitTracker.adapters.out.repository.UserMongoRepository;
import com.fais.HabitTracker.domain.models.User;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserMongoRepository userMongoRepository;

    public UserRepositoryAdapter(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userMongoRepository.getUserByUsername(username);
    }

    @Override
    public User save(User user) {
        return userMongoRepository.save(user);
    }
}
