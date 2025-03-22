package com.fais.HabitTracker.domain.ports.in;

import com.fais.HabitTracker.domain.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserPort {
    User registerUser(User user);
}
