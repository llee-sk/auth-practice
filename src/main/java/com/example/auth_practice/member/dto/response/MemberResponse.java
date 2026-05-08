package com.example.auth_practice.member.dto.response;

import com.example.auth_practice.member.enums.MemberRole;
import com.example.auth_practice.member.enums.MemberStatus;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String email,
        String name,
        MemberRole role,
        MemberStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
