package com.fais.HabitTracker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Can't find any principal")
public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException() {
        super("Can't find any principal");
    }
}
