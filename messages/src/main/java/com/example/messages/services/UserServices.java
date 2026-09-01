package com.example.messages.services;

import com.example.messages.dto.auth.UserRegistrationDTO;
import com.example.messages.entity.User;
import com.example.messages.mapper.UserMapper;
import com.example.messages.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServices(
            UserRepository userRepository,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User register(UserRegistrationDTO dto) {

        User user = userMapper.toEntity(dto);

        return userRepository.save(user);
    }
}