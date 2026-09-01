package com.example.messages.mapper;

import com.example.messages.dto.auth.UserRegistrationDTO;
import com.example.messages.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRegistrationDTO dto) {

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCreatedAt(dto.getCreateAt());

        return user;
    }
}