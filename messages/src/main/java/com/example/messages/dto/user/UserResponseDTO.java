package com.example.messages.dto.user;

import com.example.messages.entity.Roles;

import java.time.Instant;
import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String username;
    private String email;
    private Instant createdAt;
    private Roles userRole;

    public UserResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Roles getUserRole() {
        return userRole;
    }

    public void setUserRole(Roles role) {
        this.userRole = role;
    }
}