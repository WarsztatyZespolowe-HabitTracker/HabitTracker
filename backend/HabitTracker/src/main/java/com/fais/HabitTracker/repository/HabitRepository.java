package com.fais.HabitTracker.repository;

import com.fais.HabitTracker.models.habit.Habit;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HabitRepository extends MongoRepository<Habit, String> {
    List<Habit> findByUserId(String userId);

    List<Habit> findByUserIdAndDaysOfWeekContaining(String userId, String dayOfWeek);

    Optional<Habit> findByIdAndUserId(String id, String userId);
}
