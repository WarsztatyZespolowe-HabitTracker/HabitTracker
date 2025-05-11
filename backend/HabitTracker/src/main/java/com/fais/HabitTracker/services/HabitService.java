package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;

import java.util.List;

public interface HabitService {
    HabitResponseDTO createHabit(HabitRequestDTO request);

    List<HabitResponseDTO> getHabitsForUser(String userId);

    List<HabitResponseDTO> getHabitsForToday(String userId);

    int calculateStreak(String userId, String habitId);
    
    HabitResponseDTO markHabitAsSkipped(String userId, String habitId);
    
    HabitResponseDTO markHabitAsCompleted(String userId, String habitId);
}
