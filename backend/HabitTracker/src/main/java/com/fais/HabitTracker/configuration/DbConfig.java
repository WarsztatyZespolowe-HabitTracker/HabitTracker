package com.fais.HabitTracker.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.fais.HabitTracker.repository")
public class DbConfig {
}
