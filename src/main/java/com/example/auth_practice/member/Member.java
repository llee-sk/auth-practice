package com.example.auth_practice.member;

import com.example.auth_practice.global.entity.BaseTimeEntity;
import com.example.auth_practice.member.enums.MemberRole;
import com.example.auth_practice.member.enums.MemberStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "members")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 120, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    public Member(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = MemberRole.USER;
        this.status = MemberStatus.ACTIVE;
    }
}
