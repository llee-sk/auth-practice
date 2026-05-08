package com.example.auth_practice.auth.controller;

import com.example.auth_practice.auth.dto.request.LoginRequest;
import com.example.auth_practice.auth.service.LoginService;
import com.example.auth_practice.global.dto.ApiResponse;
import com.example.auth_practice.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "로그인")
    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> login(@Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(ApiResponse.success("로그인 검증에 성공했습니다.", loginService.login(request)));
    }
}
