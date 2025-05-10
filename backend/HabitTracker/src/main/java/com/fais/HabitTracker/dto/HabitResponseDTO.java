package com.fais.HabitTracker.dto;

import com.fais.HabitTracker.models.HabitHistory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HabitResponseDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private List<String> daysOfWeek;
    private int streak;
    private List<HabitHistory> history;
}
