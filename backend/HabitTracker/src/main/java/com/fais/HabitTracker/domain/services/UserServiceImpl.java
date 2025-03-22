package com.fais.HabitTracker.domain.services;

import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.adapters.in.dto.register.UserResponseDTO;
import com.fais.HabitTracker.domain.mapper.UserMapper;
import com.fais.HabitTracker.domain.models.User;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import com.fais.HabitTracker.exceptions.UserAlreadyExistsException;
import com.fais.HabitTracker.exceptions.UserNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    private final UserMapper userMapper;

    private void validateInput(String username, String password) {

    }


    public Optional<User> registerUser(String username, String password) {
        Optional<User> existingUser = userRepositoryPort.findUserByUsername(username);
        if (existingUser.isPresent()) {
            return Optional.empty();
        }
        User user = new User(null, username, password);
        return Optional.of(userRepositoryPort.save(user));
    }


    @Override
    public boolean validateUserLogin(String username, String password) {
        Optional<User> userByUsername = userRepositoryPort.findUserByUsername(username);
        return userByUsername.isPresent() && passwordEncoder.matches(password, userByUsername.get().getPassword());
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> all = userRepositoryPort.findAll();
        return userMapper.mapToResponseListDto(all);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        Optional<User> byId = userRepositoryPort.findById(id);
        return byId.map(userMapper::mapToResponseDto).orElse(null);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = userMapper.mapRequestToEntity(request);
        User savedUser = userRepositoryPort.save(user);
        return userMapper.mapToResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO request) {
        Optional<User> byId = userRepositoryPort.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();

            if (StringUtils.isNotBlank(request.username())) {
                user.setUsername(request.username());
            }

            if (StringUtils.isNotBlank(request.password())) {
                user.setPassword(request.password());
            }

            return userMapper.mapToResponseDto(user);
        }

        return null;
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> byId = userRepositoryPort.findById(id);
        if (byId.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id %s not found!", id));
        }

        User user = byId.get();
        user.setActive(false);
        user.setDeletedAt(new Date());

        userRepositoryPort.save(user);
    }

}
