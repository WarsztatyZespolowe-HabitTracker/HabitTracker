package com.fais.HabitTracker.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestApi {

    private final static String API = "/api";
    public final static String AUTH_API = API + "/auth";
}
