package com.example.auth_practice.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignupRequest {
    @NotBlank(message = "이름은 필수 입력값 입니다.")
    private String name;
    @NotBlank(message = "이메일은 필수 입력값 입니다.")
    private String email;
}
