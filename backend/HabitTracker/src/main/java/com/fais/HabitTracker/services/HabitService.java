package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.mappers.HabitMapper;
import com.fais.HabitTracker.models.habit.Habit;
import com.fais.HabitTracker.models.habit.HabitHistory;
import com.fais.HabitTracker.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    public HabitResponseDTO createHabit(HabitRequestDTO request) {
        Habit habit = habitMapper.mapRequestToEntity(request);
        habit.setHistory(new ArrayList<>());
        Habit savedHabit = habitRepository.save(habit);
        return habitMapper.mapEntityToResponse(savedHabit, 0);
    }

    public List<HabitResponseDTO> getHabitsForUser(String userId) {
        List<Habit> habits = habitRepository.findByUserId(userId);
        return habits.stream()
                .map(habit -> {
                    int streak = calculateStreak(userId, habit.getId());
                    return habitMapper.mapEntityToResponse(habit, streak);
                })
                .toList();
    }

    public List<HabitResponseDTO> getHabitsForToday(String userId) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        String todayString = today.name();

        List<Habit> habits = habitRepository.findByUserIdAndDaysOfWeekContaining(userId, todayString);
        return habits.stream()
                .map(habit -> {
                    int streak = calculateStreak(userId, habit.getId());
                    return habitMapper.mapEntityToResponse(habit, streak);
                })
                .toList();
    }

    public int calculateStreak(String userId, String habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));

        List<HabitHistory> history = habit.getHistory();
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

    public HabitResponseDTO markHabitAsSkipped(String userId, String habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        
        updateTodayHistoryRecord(habit, false, true);
        
        Habit savedHabit = habitRepository.save(habit);
        
        return habitMapper.mapEntityToResponse(savedHabit, 0);
    }

    public HabitResponseDTO markHabitAsCompleted(String userId, String habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        
        updateTodayHistoryRecord(habit, true, false);
        
        Habit savedHabit = habitRepository.save(habit);
        
        return habitMapper.mapEntityToResponse(savedHabit, 0);
    }

    private void updateTodayHistoryRecord(Habit habit, boolean completed, boolean skipped) {
        if (habit.getHistory() == null) {
            habit.setHistory(new ArrayList<>());
        }
        
        LocalDate today = LocalDate.now();
        
        habit.getHistory().stream()
                .filter(record -> isSameDate(record.getDate(), today))
                .findFirst()
                .ifPresentOrElse(
                    existingRecord -> updateExistingRecord(existingRecord, completed, skipped),
                    () -> createNewRecord(habit, completed, skipped)
                );
    }

    private void updateExistingRecord(HabitHistory existingRecord, boolean completed, boolean skipped) {
        existingRecord.setCompleted(completed);
        existingRecord.setSkipped(skipped);
    }

    private void createNewRecord(Habit habit, boolean completed, boolean skipped) {
        HabitHistory newRecord = HabitHistory.builder()
                .date(new Date())
                .completed(completed)
                .skipped(skipped)
                .build();
        habit.getHistory().add(newRecord);
    }

    private boolean isSameDate(Date date, LocalDate localDate) {
        LocalDate dateAsLocalDate = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return dateAsLocalDate.equals(localDate);
    }

    public void deleteHabit(String habitId, String userId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Habit not found or not owned by user"
                ));
        habitRepository.delete(habit);
    }

    public List<Habit> getHabitsNeedingReminder(String userId) {
        List<Habit> habits = habitRepository.findByUserId(userId);
        LocalDate today = LocalDate.now();
        String todayDay = today.getDayOfWeek().name();

        return habits.stream()
                .filter(habit -> habit.getReminder() != null && habit.getReminder().isEnabled())
                .filter(habit -> habit.getReminder().getDaysOfWeek().contains(todayDay))
                .filter(habit ->
                        habit.getHistory() == null ||
                                habit.getHistory().stream().noneMatch(entry ->
                                        isSameDay(entry.getDate(), today) && entry.isCompleted()
                                )
                )
                .toList();
    }

    private boolean isSameDay(Date date, LocalDate localDate) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().equals(localDate);
    }
}
