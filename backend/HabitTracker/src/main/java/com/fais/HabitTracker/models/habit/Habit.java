package com.fais.HabitTracker.models.habit;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
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

    private boolean reminder;
    private boolean hidden;

    public int calculateStreak() {

        List<HabitHistory> history = this.history;
        if (history == null || history.isEmpty()) {
            return 0;
        }

        history = history.stream()
                .filter(record -> record.isCompleted() || record.isSkipped())
                .sorted(Comparator.comparing(HabitHistory::getDate).reversed())
                .toList();

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        for (HabitHistory record : history) {
            LocalDate recordDate = record.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (recordDate.equals(expectedDate)) {
                if (record.isCompleted()) {
                    streak++;
                }
                expectedDate = expectedDate.minusDays(1);
            } else if (recordDate.isBefore(expectedDate)) {
                break;
            }
        }

        return streak;
    }
}
