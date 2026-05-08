package com.example.auth_practice.auth.service;

import com.example.auth_practice.auth.dto.request.LoginRequest;
import com.example.auth_practice.auth.exception.InvalidCredentialsException;
import com.example.auth_practice.member.Member;
import com.example.auth_practice.member.MemberRepository;
import com.example.auth_practice.member.dto.response.MemberResponse;
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

    public MemberResponse login(LoginRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        Member member = memberRepository.findByEmailAndStatus(email, MemberStatus.ACTIVE).orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new InvalidCredentialsException();
        }

        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getRole(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}
