package com.fais.HabitTracker.adapters.in;


import com.fais.HabitTracker.adapters.in.dto.register.UserRequestDTO;
import com.fais.HabitTracker.adapters.in.dto.register.UserResponseDTO;
import com.fais.HabitTracker.domain.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity register(@RequestBody UserRequestDTO userRequestDTO) {
        try{
            userService.registerUser(userRequestDTO.username(), userRequestDTO.password());
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
