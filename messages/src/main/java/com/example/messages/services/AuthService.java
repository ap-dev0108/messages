package com.example.messages.services;

import com.example.messages.dto.auth.LoginResponseDTO;
import com.example.messages.dto.auth.RefreshTokenResponseDTO;
import com.example.messages.dto.auth.UserLoginDTO;
import com.example.messages.dto.auth.UserRegistrationDTO;
import com.example.messages.dto.user.UserResponseDTO;
import com.example.messages.entity.RefreshToken;
import com.example.messages.entity.Roles;
import com.example.messages.entity.User;
import com.example.messages.exception.ConflictException;
import com.example.messages.exception.InvalidCredentialsException;
import com.example.messages.mapper.UserMapper;
import com.example.messages.repository.UserRepository;
import com.example.messages.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponseDTO register(UserRegistrationDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email is already registered");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new ConflictException("Username is already taken");
        }

        User user = userMapper.toEntity(dto);

        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        user.setUserRole(Roles.USER);
        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    public LoginResponseDTO login(UserLoginDTO dto) {

        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateAccessToken(user.getId(), user.getRoles().name());

        RefreshToken refreshToken = refreshTokenService.create(user);

        UserResponseDTO userResponse = userMapper.toResponseDTO(user);

        return new LoginResponseDTO(token, refreshToken.getToken(), userResponse);
    }

    public RefreshTokenResponseDTO refreshAccessToken(
            String token
    ) {

        RefreshToken refreshToken =
                refreshTokenService.validate(token);

        User user = refreshToken.getUser();

        String accessToken =
                jwtService.generateAccessToken(
                        user.getId(),
                        user.getRoles().name()
                );

        RefreshToken newRefreshToken =
                refreshTokenService.create(user);

        refreshTokenService.revoke(refreshToken);

        return new RefreshTokenResponseDTO(
                accessToken,
                newRefreshToken.getToken()
        );
    }
}