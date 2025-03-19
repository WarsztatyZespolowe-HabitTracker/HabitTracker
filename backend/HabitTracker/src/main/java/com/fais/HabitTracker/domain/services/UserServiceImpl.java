package com.fais.HabitTracker.domain.services;

import com.fais.HabitTracker.domain.models.User;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import com.fais.HabitTracker.exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{3,20}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

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

    @Override
    public boolean validateUserLogin(String username, String password) {
        Optional<User> userByUsername = userRepositoryPort.findUserByUsername(username);
        return userByUsername.isPresent() && passwordEncoder.matches(password, userByUsername.get().getPassword());
    }

}
