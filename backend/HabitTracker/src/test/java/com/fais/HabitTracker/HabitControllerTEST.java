package com.fais.HabitTracker;

import com.fais.HabitTracker.models.user.User;
import com.fais.HabitTracker.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
class HabitControllerTEST {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private MongoTemplate mongoTemplate;

    @BeforeEach
    void setup() {
        mongoTemplate.getDb().drop();
        userRepository.deleteAll();
        User user = User.createNewUser("jakub", passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    private String createHabitAndReturnId() throws Exception {
        String json = """
            {
              "name": "Testowy nawyk",
              "description": "Opis testowy",
              "category": "Zdrowie",
              "daysOfWeek": ["MONDAY", "FRIDAY"],
              "reminder": false,
              "hidden": false
            }
        """;
        var result = mockMvc.perform(post("/api/habits")
                        .with(csrf())
                        .with(httpBasic("jakub", "password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString().split("\"id\":\"")[1].split("\"")[0];
    }

    @Test void test01_createHabit() throws Exception { createHabitAndReturnId(); }

    @Test void test02_getAllHabits() throws Exception {
        createHabitAndReturnId();
        mockMvc.perform(get("/api/habits").with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test03_getHabitsForToday() throws Exception {
        createHabitAndReturnId();
        mockMvc.perform(get("/api/habits/today").with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test04_completeHabit() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(put("/api/habits/{id}/complete", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test05_skipHabit() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(put("/api/habits/{id}/skip", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test06_enableReminder() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(post("/api/habits/{id}/reminder", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test07_disableReminder() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(delete("/api/habits/{id}/reminder", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isNoContent());
    }

    @Test void test08_hideHabit() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(post("/api/habits/{id}/hide", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isOk());
    }

    @Test void test09_unhideHabit() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(delete("/api/habits/{id}/hide", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isNoContent());
    }

    @Test void test10_deleteHabit() throws Exception {
        String id = createHabitAndReturnId();
        mockMvc.perform(delete("/api/habits/{id}", id).with(csrf()).with(httpBasic("jakub", "password"))).andExpect(status().isNoContent());
    }
}
