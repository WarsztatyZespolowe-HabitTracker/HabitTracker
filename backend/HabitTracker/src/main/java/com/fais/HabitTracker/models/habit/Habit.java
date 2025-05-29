package com.fais.HabitTracker.models.habit;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "habits")
public class Habit {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String userId;
    private List<String> daysOfWeek;
    private List<HabitHistory> history;
}
