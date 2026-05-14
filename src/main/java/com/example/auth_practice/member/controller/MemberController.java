package com.example.auth_practice.member.controller;

import com.example.auth_practice.global.dto.ApiResponse;
import com.example.auth_practice.member.dto.response.MemberResponse;
import com.example.auth_practice.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멤버 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getMyInfo(Authentication authentication){
        Long id = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("내 정보 조회 완료", memberService.getMyInfo(id)));
    }
}
