package com.fais.HabitTracker.dto;

import com.fais.HabitTracker.enums.Role;

public record UserResponseDTO(String id, String username, boolean active, Role role) {}
