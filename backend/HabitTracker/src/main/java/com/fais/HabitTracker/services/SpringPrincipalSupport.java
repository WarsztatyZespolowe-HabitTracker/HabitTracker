package com.fais.HabitTracker.services;

import com.fais.HabitTracker.exceptions.ForbiddenException;
import com.fais.HabitTracker.exceptions.UnAuthorizedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;

public interface SpringPrincipalSupport {

    default <T extends Principal> T getPrincipal(boolean required) {
        @SuppressWarnings("unchecked") final T principal = (T) SecurityContextHolder.getContext().getAuthentication();
        if (required && (principal == null || principal instanceof AnonymousAuthenticationToken)) {
            throw new UnAuthorizedException();
        }

        try {
            return principal;
        } catch (Exception e) {
            throw new ForbiddenException();
        }
    }
}
