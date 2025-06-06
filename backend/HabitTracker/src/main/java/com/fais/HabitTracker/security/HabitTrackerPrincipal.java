package com.fais.HabitTracker.security;

import com.fais.HabitTracker.models.user.User;
import com.fais.HabitTracker.exceptions.UnAuthorizedException;
import lombok.Getter;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Function;

public record HabitTrackerPrincipal(@Getter String id, Principal principal) implements Principal {

    public static HabitTrackerPrincipal create(Principal principal, Function<String, Optional<User>> userProvider) {
        if (principal == null) {
            throw new UnAuthorizedException();
        }

        String username = principal.getName();
        Optional<User> optionalUser = userProvider.apply(username);
        if (optionalUser.isEmpty()) {
            throw new UnAuthorizedException();
        }

        User user = optionalUser.get();

        return new HabitTrackerPrincipal(user.getId(), principal);

    }

    @Override
    public String getName() {
        return principal.getName();
    }
}
