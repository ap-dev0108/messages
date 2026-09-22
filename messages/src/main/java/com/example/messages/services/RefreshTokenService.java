package com.example.messages.services;

import com.example.messages.entity.RefreshToken;
import com.example.messages.entity.User;
import com.example.messages.repository.RefreshTokenRepository;
import com.example.messages.security.JwtService;

import java.time.Instant;

public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
            RefreshTokenRepository refreshToken,
            JwtService jwtService
    ) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshToken;
    }

    public RefreshToken create(User user) {

        RefreshToken refreshToken =
                new RefreshToken();

        refreshToken.setUser(user);

        refreshToken.setToken(
                jwtService.generateRefreshToken()
        );

        refreshToken.setExpiresAt(
                Instant.now().plusMillis(
                        jwtService.getRefreshTokenExpiration()
                )
        );

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken validate(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.isRevoked()) {
            throw new IllegalArgumentException(
                    "Refresh token has been revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new IllegalArgumentException(
                    "Refresh token has expired"
            );
        }

        return refreshToken;
    }

    public void revoke(RefreshToken refreshToken) {

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }

    public void revokeByToken(String token) {

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByToken(token)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Invalid refresh token"
                                )
                        );

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);
    }
}
