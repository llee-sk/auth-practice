package com.example.auth_practice.member.controller;

import com.example.auth_practice.auth.session.SessionConst;
import com.example.auth_practice.auth.session.SessionMember;
import com.example.auth_practice.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "멤버 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberController {

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<SessionMember>> getMyInfo(HttpServletRequest request){
        SessionMember sessionMember = (SessionMember) request.getAttribute(SessionConst.REQUEST_MEMBER);
        return ResponseEntity.ok(ApiResponse.success("현재 로그인 사용자 조회에 성공했습니다.", sessionMember));
    }
}
