package com.fais.HabitTracker.mappers;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.models.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HabitMapper {
    Habit mapRequestToEntity(HabitRequestDTO dto);

    @Mapping(target = "streak", source = "streak")
    HabitResponseDTO mapEntityToResponse(Habit habit, int streak);

    List<HabitResponseDTO> mapEntityListToResponseList(List<Habit> habits);
}
