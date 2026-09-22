package com.example.messages.dto.user;

import java.util.UUID;

public class OtherUserResponseDTO {

    private UUID id;
    private String username;

    public OtherUserResponseDTO(
            UUID id,
            String username
    ) {
        this.id = id;
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}