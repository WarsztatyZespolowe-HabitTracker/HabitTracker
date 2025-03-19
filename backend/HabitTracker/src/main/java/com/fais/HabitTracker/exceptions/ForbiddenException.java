package com.fais.HabitTracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Insufficient credentials")
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super("Insufficient credentials");
    }
}
