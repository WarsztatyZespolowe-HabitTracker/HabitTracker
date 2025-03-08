package com.fais.HabitTracker.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    String name;
    String email;
    String password;
    public User(String name, String email, String password) {}

}
