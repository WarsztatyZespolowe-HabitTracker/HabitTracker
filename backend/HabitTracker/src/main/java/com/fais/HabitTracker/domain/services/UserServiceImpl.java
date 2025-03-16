package com.fais.HabitTracker.domain.services;

import com.fais.HabitTracker.domain.models.User;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import com.fais.HabitTracker.exceptions.UserAlreadyExistsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;


@Service
public class UserServiceImpl implements UserService {

    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{3,20}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private UserRepositoryPort userRepositoryPort;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    private void validateInput(String username, String password) {

    }


    public User registerUser(String username, String password) {
        validateInput(username, password);

        if (userRepositoryPort.findUserByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User exsist");
        }

        User newUser = new User(username, passwordEncoder.encode(password));
        return userRepositoryPort.save(newUser);
    }

}
