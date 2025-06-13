package com.fais.HabitTracker.mappers;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.models.habit.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitMapper {

    Habit mapRequestToEntity(HabitRequestDTO dto);

    @Mapping(target = "streak", source = "streak")
    @Mapping(target = "reminder", source = "habit.reminder")
    @Mapping(target = "hidden", source = "habit.hidden")
    HabitResponseDTO mapEntityToResponse(Habit habit, int streak);

    List<HabitResponseDTO> mapEntityListToResponseList(List<Habit> habits);
}
