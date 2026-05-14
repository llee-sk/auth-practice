package com.example.auth_practice.member.service;

import com.example.auth_practice.member.Member;
import com.example.auth_practice.member.MemberRepository;
import com.example.auth_practice.member.dto.response.MemberResponse;
import com.example.auth_practice.member.enums.MemberStatus;
import com.example.auth_practice.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponse getMyInfo(Long id) {
        Member member = memberRepository.findByIdAndStatus(id, MemberStatus.ACTIVE).orElseThrow(MemberNotFoundException::new);
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
