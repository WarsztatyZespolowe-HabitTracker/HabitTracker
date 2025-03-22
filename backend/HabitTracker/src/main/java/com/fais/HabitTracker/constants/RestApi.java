package com.fais.HabitTracker.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestApi {

    private final static String API = "/api";
    private final static String ADMIN_API = "/api/admin";

    public final static String AUTH_API = API + "/auth";
    public final static String USERS_ADMIN_API = ADMIN_API + "/users";
}
