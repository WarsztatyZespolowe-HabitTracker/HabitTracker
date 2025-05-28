package com.fais.HabitTracker.models.habit;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HabitHistory {
    private Date date;
    private boolean completed;
    private boolean skipped;
}
