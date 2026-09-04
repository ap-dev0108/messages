package com.example.messages.services;

import com.example.messages.dto.auth.UserRegistrationDTO;
import com.example.messages.dto.user.UserResponseDTO;
import com.example.messages.entity.User;
import com.example.messages.exception.ResourceNotFoundException;
import com.example.messages.mapper.UserMapper;
import com.example.messages.repository.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServices {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServices( UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO getMyProfile(
            @NonNull Authentication authentication
    ) {

        UUID userId = (UUID) authentication.getPrincipal();
        System.out.println("Got the following ID: " + userId);

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return userMapper.toResponseDTO(user);
    }
}