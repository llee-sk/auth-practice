package com.example.auth_practice.auth.controller;

import com.example.auth_practice.auth.dto.request.RefreshTokenRequest;
import com.example.auth_practice.auth.dto.response.MemberSignupResponse;
import com.example.auth_practice.auth.dto.response.TokenResponse;
import com.example.auth_practice.auth.service.AuthService;
import com.example.auth_practice.auth.service.SignUpService;
import com.example.auth_practice.auth.dto.request.MemberSignupRequest;
import com.example.auth_practice.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "인증 관련 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final SignUpService signUpService;
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberSignupResponse>> signup(@Valid @RequestBody MemberSignupRequest request){
        return ResponseEntity.ok(ApiResponse.success("회원가입에 성공했습니다.", signUpService.signup(request)));
    }

    @Operation(summary = "Access Token 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@Valid @RequestBody RefreshTokenRequest request){
        String refreshToken = request.getRefreshToken();
        return ResponseEntity.ok(ApiResponse.success("Access Token 재발급 성공했습니다.", authService.reissue(refreshToken)));
    }
}
