package com.fais.HabitTracker;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.enums.Role;
import com.fais.HabitTracker.mappers.UserMapper;
import com.fais.HabitTracker.models.User;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

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



    @Test
    void shouldNotRegisterUserWhenUsernameExists() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> registeredUser = userService.registerUser("testuser", "password123");

        assertTrue(registeredUser.isEmpty());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldValidateUserLoginSuccessfully() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);

        boolean isValid = userService.validateUserLogin("testuser", "password123");

        assertTrue(isValid);
    }

    @Test
    void shouldNotValidateUserLoginWhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        boolean isValid = userService.validateUserLogin("testuser", "password123");

        assertFalse(isValid);
    }


}
