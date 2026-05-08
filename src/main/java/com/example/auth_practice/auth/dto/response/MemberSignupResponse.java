package com.example.auth_practice.auth.dto.response;

import com.example.auth_practice.member.enums.MemberRole;
import com.example.auth_practice.member.enums.MemberStatus;

import java.time.Instant;

public record MemberSignupResponse(
        Long id,
        String name,
        String email,
        MemberRole role,
        MemberStatus status,
        Instant createdAt
) {
}
