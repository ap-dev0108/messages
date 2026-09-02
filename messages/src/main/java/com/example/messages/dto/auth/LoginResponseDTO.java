package com.example.messages.dto.auth;

import com.example.messages.dto.user.UserResponseDTO;

public class LoginResponseDTO {

    private String accessToken;
    private UserResponseDTO user;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String accessToken, UserResponseDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}