package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.mappers.HabitMapper;
import com.fais.HabitTracker.models.Habit;
import com.fais.HabitTracker.models.HabitHistory;
import com.fais.HabitTracker.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;

    @Override
    public HabitResponseDTO createHabit(HabitRequestDTO request) {
        Habit habit = habitMapper.mapRequestToEntity(request);
        habit.setHistory(new ArrayList<>());
        Habit savedHabit = habitRepository.save(habit);
        return habitMapper.mapEntityToResponse(savedHabit, 0);
    }

    @Override
    public List<HabitResponseDTO> getHabitsForUser(String userId) {
        List<Habit> habits = habitRepository.findByUserId(userId);
        return habits.stream()
                .map(habit -> {
                    int streak = calculateStreak(userId, habit.getId());
                    return habitMapper.mapEntityToResponse(habit, streak);
                })
                .toList();
    }

    @Override
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

    @Override
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
                .collect(Collectors.toList());

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

    @Override
    public HabitResponseDTO markHabitAsSkipped(String userId, String habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        
        updateTodayHistoryRecord(habit, false, true);
        
        Habit savedHabit = habitRepository.save(habit);
        
        int streak = calculateStreak(userId, habitId);
        return habitMapper.mapEntityToResponse(savedHabit, streak);
    }

    @Override
    public HabitResponseDTO markHabitAsCompleted(String userId, String habitId) {
        Habit habit = habitRepository.findByIdAndUserId(habitId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Habit not found"));
        
        // Tworzymy nowy rekord historii z datą dzisiejszą
        HabitHistory completedRecord = HabitHistory.builder()
                .date(new Date())
                .completed(true)
                .skipped(false)
                .build();
        
        // Jeśli lista historii nie istnieje, tworzymy nową
        if (habit.getHistory() == null) {
            habit.setHistory(new ArrayList<>());
        }
        
        // Sprawdzamy czy już nie ma dzisiejszej daty
        LocalDate today = LocalDate.now();
        boolean hasEntryForToday = habit.getHistory().stream()
                .anyMatch(h -> {
                    LocalDate historyDate = h.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return historyDate.equals(today);
                });
        
        if (!hasEntryForToday) {
            habit.getHistory().add(completedRecord);
        } else {
            // Aktualizujemy istniejący wpis na dzisiaj
            habit.getHistory().stream()
                .filter(h -> {
                    LocalDate historyDate = h.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return historyDate.equals(today);
                })
                .findFirst()
                .ifPresent(h -> {
                    h.setCompleted(true);
                    h.setSkipped(false);
                });
        }
        
        Habit savedHabit = habitRepository.save(habit);
        
        int streak = calculateStreak(userId, habitId);
        return habitMapper.mapEntityToResponse(savedHabit, streak);
    }
}
