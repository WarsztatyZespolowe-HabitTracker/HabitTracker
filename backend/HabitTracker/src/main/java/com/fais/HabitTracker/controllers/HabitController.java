package com.fais.HabitTracker.controllers;

import com.fais.HabitTracker.dto.HabitPatchDTO;
import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.models.habit.Habit;
import com.fais.HabitTracker.services.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitService habitService;

    @PostMapping
    public HabitResponseDTO createHabit(@Valid @RequestBody HabitRequestDTO request, Principal principal) {
        String userId = principal.getName();
        request.setUserId(userId);
        return habitService.createHabit(request);
    }

    @GetMapping
    public List<HabitResponseDTO> getHabitsForUser(Principal principal) {
        String userId = principal.getName();
        return habitService.getHabitsForUser(userId);
    }

    @GetMapping("/today")
    public List<HabitResponseDTO> getHabitsForToday(Principal principal) {
        String userId = principal.getName();
        return habitService.getHabitsForToday(userId);
    }

    @PutMapping("/{habitId}/skip")
    public HabitResponseDTO markHabitAsSkipped(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        return habitService.markHabitAsSkipped(userId, habitId);
    }

    @PutMapping("/{habitId}/complete")
    public HabitResponseDTO markHabitAsCompleted(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        return habitService.markHabitAsCompleted(userId, habitId);
    }

    @DeleteMapping("/{habitId}")
    public ResponseEntity<Void> deleteHabit(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.deleteHabit(habitId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{habitId}/reminder")
    public ResponseEntity<Void> enableReminder(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.setReminder(habitId, userId, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{habitId}/reminder")
    public ResponseEntity<Void> disableReminder(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.setReminder(habitId, userId, false);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{habitId}/hide")
    public ResponseEntity<Void> hideHabit(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.setHidden(habitId, userId, true);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{habitId}/hide")
    public ResponseEntity<Void> unhideHabit(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.setHidden(habitId, userId, false);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{habitId}")
    public HabitResponseDTO partiallyUpdateHabit(@PathVariable String habitId,
                                                 @RequestBody HabitPatchDTO request,
                                                 Principal principal) {
        String userId = principal.getName();
        return habitService.partiallyUpdateHabit(habitId, userId, request);
    }

    @DeleteMapping("/{habitId}/reset")
    public ResponseEntity<Void> resetHabit(@PathVariable String habitId, Principal principal) {
        String userId = principal.getName();
        habitService.resetHabitHistory(habitId, userId);
        return ResponseEntity.noContent().build();
    }


}
