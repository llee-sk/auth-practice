package com.example.auth_practice.auth.service;

import com.example.auth_practice.auth.domain.RefreshToken;
import com.example.auth_practice.auth.dto.response.TokenResponse;
import com.example.auth_practice.auth.exception.InvalidRefreshTokenException;
import com.example.auth_practice.auth.exception.RefreshTokenMismatchException;
import com.example.auth_practice.auth.exception.RefreshTokenNotFoundException;
import com.example.auth_practice.auth.repository.RefreshTokenRepository;
import com.example.auth_practice.global.jwt.JwtTokenProvider;
import com.example.auth_practice.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public TokenResponse reissue(String requestRefreshToken) {
        validateRefreshToken(requestRefreshToken);

        RefreshToken savedRefreshToken = getRefreshToken(requestRefreshToken);

        Member member = savedRefreshToken.getMember();
        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail(), member.getRole());

        String reissuedRefreshToken = jwtTokenProvider.createRefreshToken(member.getId());
        Instant expiresAt = Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpirationTime());

        savedRefreshToken.updateRefreshToken(reissuedRefreshToken, expiresAt);

        return new TokenResponse(
                "Bearer",
                accessToken,
                reissuedRefreshToken,
                jwtTokenProvider.getAccessTokenExpirationTime()
        );
    }

    private RefreshToken getRefreshToken(String requestRefreshToken) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(requestRefreshToken);
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(memberId).orElseThrow(RefreshTokenNotFoundException::new);
        if (!savedRefreshToken.getRefreshToken().equals(requestRefreshToken)) {
            throw new RefreshTokenMismatchException();
        }
        return savedRefreshToken;
    }

    private void validateRefreshToken(String requestRefreshToken) {
        if (!StringUtils.hasText(requestRefreshToken) || !jwtTokenProvider.validateToken(requestRefreshToken)) {
            throw new InvalidRefreshTokenException();
        }
    }

    @Transactional
    public void logout(String requestRefreshToken) {
        validateRefreshToken(requestRefreshToken);
        RefreshToken savedRefreshToken = getRefreshToken(requestRefreshToken);

        refreshTokenRepository.delete(savedRefreshToken);
    }
}
