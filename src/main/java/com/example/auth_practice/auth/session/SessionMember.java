package com.example.auth_practice.auth.session;

import com.example.auth_practice.member.enums.MemberRole;

public record SessionMember(
        Long id,
        String email,
        String name,
        MemberRole role
) {
}
