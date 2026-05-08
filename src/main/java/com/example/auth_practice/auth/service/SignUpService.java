package com.example.auth_practice.auth.service;

import com.example.auth_practice.auth.dto.MemberSignupRequest;
import com.example.auth_practice.auth.dto.MemberSignupResponse;
import com.example.auth_practice.auth.exception.DuplicationEmailException;
import com.example.auth_practice.member.Member;
import com.example.auth_practice.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberSignupResponse signup(MemberSignupRequest request) {
        String email = request.getEmail().toLowerCase().trim();
        if (memberRepository.existsByEmail(email)){
            throw new DuplicationEmailException("이미 사용 중인 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member(
                request.getName(),
                email,
                encodedPassword
        );

        memberRepository.save(member);

        return new MemberSignupResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole(),
                member.getStatus(),
                member.getCreatedAt()
        );
    }
}
