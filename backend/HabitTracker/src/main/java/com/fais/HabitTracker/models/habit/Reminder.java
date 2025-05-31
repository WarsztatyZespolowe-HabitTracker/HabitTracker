package com.fais.HabitTracker.models.habit;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {
    private boolean enabled;
    private String time;
    private List<String> daysOfWeek;
}
