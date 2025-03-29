package com.fais.HabitTracker;

import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.adapters.in.dto.register.UserResponseDTO;
import com.fais.HabitTracker.domain.mapper.UserMapper;
import com.fais.HabitTracker.domain.models.User;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import com.fais.HabitTracker.domain.services.UserServiceImpl;
import com.fais.HabitTracker.enums.Role;
import com.fais.HabitTracker.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLogicTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("123");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setRole(Role.USER);

        userRequestDTO = new UserRequestDTO("testuser", "password123");
        userResponseDTO = new UserResponseDTO("123", "testuser", true, Role.USER);
    }


// registration

    @Test
    void shouldNotRegisterUserWhenEmptyPassword() {
        when(userRepositoryPort.findUserByUsername("testuser")).thenReturn(Optional.empty());

        Optional<User> registeredUser = userService.registerUser("testuser", "");

        assertTrue(registeredUser.isEmpty());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    void shouldNotRegisterUserWhenEmptyName() {
        when(userRepositoryPort.findUserByUsername("")).thenReturn(Optional.empty());

        Optional<User> registeredUser = userService.registerUser("", "password123");

        assertTrue(registeredUser.isEmpty());
        verify(userRepositoryPort, never()).save(any(User.class));
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepositoryPort.findUserByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepositoryPort.save(any(User.class))).thenReturn(user);

        Optional<User> registeredUser = userService.registerUser("testuser", "password123");

        assertTrue(registeredUser.isPresent());
        assertEquals("testuser", registeredUser.get().getUsername());
        verify(userRepositoryPort, times(1)).save(any(User.class));
    }

    @Test
    void shouldNotLoginWhenPasswordIsWrong() {
        when(userRepositoryPort.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        boolean isValid = userService.validateUserLogin("testuser", "wrongpassword");

        assertFalse(isValid);
        verify(userRepositoryPort, never()).save(any(User.class));
    }


// old tests

    // void shouldNotRegisterUserWhenUsernameExists() {

    // void shouldValidateUserLoginSuccessfully() {

    // void shouldNotValidateUserLoginWhenUserNotFound() {

}

