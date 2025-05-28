package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.exceptions.UserNotFoundException;
import com.fais.HabitTracker.mappers.UserMapper;
import com.fais.HabitTracker.models.user.User;
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
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public User registerUser(String username, String password) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return null;
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
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

        return updateUserData(byId, request);
    }

    @Override
    public UserResponseDTO updateUserByName(String username, UserRequestDTO request) {
        Optional<User> byUsername = userRepository.findByUsername(username);

        return updateUserData(byUsername, request);
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id %s not found!", id));
        }

        User user = byId.get();
        user.deactivate();

        userRepository.save(user);
    }

    private UserResponseDTO updateUserData(Optional<User> optionalUser, UserRequestDTO request) {
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String rawPassword = request.password();
            String encodedPassword = StringUtils.isNotBlank(rawPassword) ? passwordEncoder.encode(rawPassword) : null;

            user.updateUserDetails(request.username(), encodedPassword);

            return userMapper.mapToResponseDto(user);

        }
        return null;
    }


}
