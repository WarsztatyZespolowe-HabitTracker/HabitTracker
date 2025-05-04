package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.exceptions.UserNotFoundException;
import com.fais.HabitTracker.mappers.UserMapper;
import com.fais.HabitTracker.models.User;
import com.fais.HabitTracker.repository.UserRepository;
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
/*
TODO: what is a reason for having interface with single implementation?
InterfaceImpl is not a desired name pattern - it just tells that class implements interface.
 */
public class UserServiceImpl implements UserService {

    // very nice usage of precompiled pattern
    private static final Pattern VALID_USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]{3,20}$");
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private void validateInput(String username, String password) {

    }


    public Optional<User> registerUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return Optional.empty();
        }
        User user = new User();
        /*
        TODO: alternative could be a factory method in User class.
              this will keep all creation logic and responsibility in one place
         */
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return Optional.of(userRepository.save(user));
    }


    @Override
    public boolean validateUserLogin(String username, String password) {
        Optional<User> userByUsername = userRepository.findByUsername(username);
        return userByUsername.isPresent() && passwordEncoder.matches(password, userByUsername.get().getPassword());
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> all = userRepository.findAll();
        return userMapper.mapToResponseListDto(all);
    }

    @Override
    public UserResponseDTO getUserById(String id) {
        Optional<User> byId = userRepository.findById(id);
        return byId.map(userMapper::mapToResponseDto).orElse(null);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = userMapper.mapRequestToEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.mapToResponseDto(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO request) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();

            /*
            TODO: Please try to group it into updateUser method in User class.
            this will be more OO as it will keep responsibility and behaviour in one place

             */
            if (StringUtils.isNotBlank(request.username())) {
                // TODO: Is username unique?
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
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id %s not found!", id));
        }

        User user = byId.get();
        /*
        TODO: user.deactivate() and in User class:
        void deactivate() {
            this.active = false;
            this.deletedAt = Instant.now();
        }
         */
        user.setActive(false);
        user.setDeletedAt(new Date());

        userRepository.save(user);
    }

}
