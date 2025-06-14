package com.fais.HabitTracker.services;

import com.fais.HabitTracker.dto.HabitPatchDTO;
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
                    int streak = habit.calculateStreak();
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
                    int streak = habit.calculateStreak();
                    return habitMapper.mapEntityToResponse(habit, streak);
                })
                .toList();
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

    public void setReminder(String habitId, String userId, boolean value) {
        Habit habit = habitRepository.findById(habitId)
                .filter(h -> h.getUserId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));
        habit.setReminder(value);
        habitRepository.save(habit);
    }

    public void setHidden(String habitId, String userId, boolean value) {
        Habit habit = habitRepository.findById(habitId)
                .filter(h -> h.getUserId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));
        habit.setHidden(value);
        habitRepository.save(habit);
    }

    public HabitResponseDTO partiallyUpdateHabit(String habitId, String userId, HabitPatchDTO dto) {
        Habit habit = habitRepository.findById(habitId)
                .filter(h -> h.getUserId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));

        if (dto.getName() != null) habit.setName(dto.getName());
        if (dto.getDescription() != null) habit.setDescription(dto.getDescription());
        if (dto.getCategory() != null) habit.setCategory(dto.getCategory());
        if (dto.getDaysOfWeek() != null) habit.setDaysOfWeek(dto.getDaysOfWeek());

        habitRepository.save(habit);

        int streak = habit.calculateStreak();
        return habitMapper.mapEntityToResponse(habit, streak);
    }

    public void resetHabitHistory(String habitId, String userId) {
        Habit habit = habitRepository.findById(habitId)
                .filter(h -> h.getUserId().equals(userId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Habit not found"));

        habit.setHistory(List.of());
        habitRepository.save(habit);
    }


}
