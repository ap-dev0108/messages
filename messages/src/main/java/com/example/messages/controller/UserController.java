package com.example.messages.controller;

import com.example.messages.dto.auth.UserRegistrationDTO;
import com.example.messages.entity.User;
import com.example.messages.services.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody UserRegistrationDTO dto
    ) {
        User user = userService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }
}