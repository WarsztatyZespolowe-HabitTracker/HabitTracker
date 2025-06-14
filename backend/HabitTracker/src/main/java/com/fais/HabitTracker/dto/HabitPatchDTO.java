package com.fais.HabitTracker.dto;

import lombok.Data;

import java.util.List;

@Data
public class HabitPatchDTO {
    private String name;
    private String description;
    private String category;
    private List<String> daysOfWeek;
}
