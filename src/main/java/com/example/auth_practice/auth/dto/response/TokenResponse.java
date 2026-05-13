package com.example.auth_practice.auth.dto.response;

public record TokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn
) {
}
