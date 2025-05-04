package com.fais.HabitTracker.models;
// TODO: please consider moving all that habit-related staff to dedicated package

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "habits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    @Id
    private String id;
    private String name;
    private String userId; // TODO: this is a good separation, no DB relation here.
    private LocalDateTime createdAt = LocalDateTime.now();
}
