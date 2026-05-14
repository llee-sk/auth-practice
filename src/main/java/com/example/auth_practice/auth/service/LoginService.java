package com.example.auth_practice.auth.service;

import com.example.auth_practice.auth.domain.RefreshToken;
import com.example.auth_practice.auth.dto.request.LoginRequest;
import com.example.auth_practice.auth.dto.response.TokenResponse;
import com.example.auth_practice.auth.exception.InvalidCredentialsException;
import com.example.auth_practice.auth.repository.RefreshTokenRepository;
import com.example.auth_practice.global.jwt.JwtTokenProvider;
import com.example.auth_practice.member.Member;
import com.example.auth_practice.member.MemberRepository;
import com.example.auth_practice.member.enums.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        Member member = memberRepository.findByEmailAndStatus(email, MemberStatus.ACTIVE).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(
                member.getId(),
                member.getEmail(),
                member.getRole()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        Instant expiresAt = Instant.now().plusMillis(jwtTokenProvider.getRefreshTokenExpirationTime());
        RefreshToken savedRefreshToken = refreshTokenRepository.findById(member.getId())
                .map(existingToken -> {
                    existingToken.updateRefreshToken(refreshToken, expiresAt);
                    return existingToken;
                })
                .orElseGet(() -> new RefreshToken(member, refreshToken, expiresAt));

        refreshTokenRepository.save(savedRefreshToken);

        return new TokenResponse(
                "Bearer",
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationTime()
        );
    }
}
