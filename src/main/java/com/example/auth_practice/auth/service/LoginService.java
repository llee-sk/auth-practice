package com.example.auth_practice.auth.service;

import com.example.auth_practice.auth.dto.request.LoginRequest;
import com.example.auth_practice.auth.dto.response.TokenResponse;
import com.example.auth_practice.auth.exception.InvalidCredentialsException;
import com.example.auth_practice.global.jwt.JwtTokenProvider;
import com.example.auth_practice.member.Member;
import com.example.auth_practice.member.MemberRepository;
import com.example.auth_practice.member.enums.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        Member member = memberRepository.findByEmailAndStatus(email, MemberStatus.ACTIVE).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new InvalidCredentialsException();
        }

        String accessToken = jwtTokenProvider.createAccessToken(member.getId(), member.getEmail(), member.getRole());
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        return new TokenResponse(
                "Bearer",
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpirationTime()
        );
    }
}
