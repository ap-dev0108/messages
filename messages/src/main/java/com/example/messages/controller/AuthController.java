package com.example.messages.controller;

import com.example.messages.dto.Response;
import com.example.messages.dto.auth.*;
import com.example.messages.dto.user.UserResponseDTO;
import com.example.messages.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody UserRegistrationDTO dto
    ) {
        UserResponseDTO response = authService.register(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody UserLoginDTO dto
    ) {
        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response<RefreshTokenResponseDTO>> refresh(
            @Valid @RequestBody RefreshTokenRequestDTO request
    ) {

        RefreshTokenResponseDTO response =
                authService.refreshAccessToken(
                        request.getRefreshToken()
                );

        return ResponseEntity.ok(
                new Response<>(
                        true,
                        "Token refreshed successfully",
                        response,
                        Instant.now()
                )
        );
    }
}