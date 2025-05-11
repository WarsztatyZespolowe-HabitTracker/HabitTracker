package com.fais.HabitTracker.controllers;

import com.fais.HabitTracker.dto.HabitRequestDTO;
import com.fais.HabitTracker.dto.HabitResponseDTO;
import com.fais.HabitTracker.services.HabitService;
import lombok.RequiredArgsConstructor;
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
}
