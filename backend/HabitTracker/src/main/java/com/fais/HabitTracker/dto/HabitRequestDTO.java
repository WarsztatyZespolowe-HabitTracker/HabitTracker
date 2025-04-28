package com.fais.HabitTracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class HabitRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotNull(message = "Days of week cannot be null")
    @Size(min = 1, message = "At least one day of the week must be specified")
    private List<String> daysOfWeek;

    private String userId;
}
