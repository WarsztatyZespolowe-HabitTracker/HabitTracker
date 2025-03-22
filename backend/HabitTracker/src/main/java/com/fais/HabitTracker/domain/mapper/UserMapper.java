package com.fais.HabitTracker.domain.mapper;

import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.adapters.in.dto.register.UserResponseDTO;
import com.fais.HabitTracker.domain.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapResponseToEntity(UserResponseDTO dto);

    UserResponseDTO mapToResponseDto(User user);
    List<UserResponseDTO> mapToResponseListDto(List<User> users);

    User mapRequestToEntity(UserRequestDTO dto);
}
