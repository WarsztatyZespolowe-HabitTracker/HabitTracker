package com.fais.HabitTracker.mappers;

import com.fais.HabitTracker.dto.UserRequestDTO;
import com.fais.HabitTracker.dto.UserResponseDTO;
import com.fais.HabitTracker.models.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User mapResponseToEntity(UserResponseDTO dto);

    UserResponseDTO mapToResponseDto(User user);
    List<UserResponseDTO> mapToResponseListDto(List<User> users);

    User mapRequestToEntity(UserRequestDTO dto);
}
