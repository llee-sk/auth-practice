package com.example.auth_practice.auth.domain;

import com.example.auth_practice.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "refresh_token", nullable = false, length = 1000)
    private String refreshToken;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    public RefreshToken(Member member, String refreshToken, Instant expiresAt) {
        this.member = member;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public void updateRefreshToken(String refreshToken, Instant expiresAt) {
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }
}
