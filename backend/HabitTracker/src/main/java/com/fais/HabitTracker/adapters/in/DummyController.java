package com.fais.HabitTracker.adapters.in;

import com.fais.HabitTracker.commons.HabitTrackerPrincipal;
import com.fais.HabitTracker.domain.ports.out.UserRepositoryPort;
import com.fais.HabitTracker.domain.services.SpringPrincipalSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dummy")
@RequiredArgsConstructor
public class DummyController implements SpringPrincipalSupport {

    private final UserRepositoryPort userRepositoryPort;


    @GetMapping()
    public ResponseEntity<?> dummy() {
        HabitTrackerPrincipal principal = HabitTrackerPrincipal.create(getPrincipal(true), userRepositoryPort::findUserByUsername);
        String id = principal.getId();
        return ResponseEntity.ok(id);
    }
}
