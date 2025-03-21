package com.fais.HabitTracker.adapters.in.dto.register;

import com.fais.HabitTracker.enums.Role;

public record UserResponseDTO(String id, String username, boolean active, Role role) {}
