package com.example.messages.controller;

import com.example.messages.dto.user.UserResponseDTO;
import com.example.messages.services.UserServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserServices userService;

    public UserController(UserServices userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyProfile(
            Authentication authentication
    ) {
        System.out.println("Authentication from controller: " + authentication);
        UserResponseDTO response =
                userService.getMyProfile(authentication);

        return ResponseEntity.ok(response);
    }
}