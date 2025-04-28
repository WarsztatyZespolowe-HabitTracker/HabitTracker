package com.fais.HabitTracker.mappers;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.models.Habit;
import org.springframework.stereotype.Component;

@Component
public class HabitMapper {
    public Habit mapRequestToEntity(HabitRequestDTO dto) {
        return Habit.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .daysOfWeek(dto.getDaysOfWeek())
                .userId(dto.getUserId())
                .build();
    }

    public HabitResponseDTO mapEntityToResponse(Habit habit, int streak) {
        return HabitResponseDTO.builder()
                .id(habit.getId())
                .name(habit.getName())
                .description(habit.getDescription())
                .category(habit.getCategory())
                .daysOfWeek(habit.getDaysOfWeek())
                .streak(streak)
                .history(habit.getHistory())
                .build();
    }
}
