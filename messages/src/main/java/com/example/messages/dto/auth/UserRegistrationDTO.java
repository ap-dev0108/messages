package com.example.messages.dto.auth;

import java.time.Instant;

public class UserRegistrationDTO {

    private String username;
    private String email;
    private String password;
    private Instant createAt;

    public UserRegistrationDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Instant getCreateAt() {
        return createAt;
    }
    public void setCreateAt(Instant date) {
        this.createAt = date;
    }
}