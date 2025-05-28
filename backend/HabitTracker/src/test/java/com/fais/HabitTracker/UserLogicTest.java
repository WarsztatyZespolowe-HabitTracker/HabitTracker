package com.fais.HabitTracker;

import com.fais.HabitTracker.enums.Role;
import com.fais.HabitTracker.models.user.User;
import com.fais.HabitTracker.repository.UserRepository;
import com.fais.HabitTracker.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserLogicTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("123");
        user.setUsername("testuser");
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setRole(Role.USER);
    }


// registration

    @Test
    void shouldNotRegisterUserWhenEmptyPassword() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        User user = userService.registerUser("testuser", "");

        assertNull(user);
    }

    @Test
    void shouldNotRegisterUserWhenEmptyName() {
        when(userRepository.findByUsername("")).thenReturn(Optional.empty());

        User user = userService.registerUser("", "password123");

        assertNull(user);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User user = userService.registerUser("testuser", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldNotLoginWhenPasswordIsWrong() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        boolean isValid = userService.validateUserLogin("testuser", "wrongpassword");

        assertFalse(isValid);
        verify(userRepository, never()).save(any(User.class));
    }

}

