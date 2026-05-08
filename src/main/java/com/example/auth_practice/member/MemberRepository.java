package com.example.auth_practice.member;

import com.example.auth_practice.member.enums.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    Optional<Member> findByEmailAndStatus(String email, MemberStatus memberStatus);
}
