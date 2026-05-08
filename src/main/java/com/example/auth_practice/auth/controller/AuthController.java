package com.example.auth_practice.auth.controller;

import com.example.auth_practice.auth.dto.response.MemberSignupResponse;
import com.example.auth_practice.auth.service.SignUpService;
import com.example.auth_practice.auth.dto.request.MemberSignupRequest;
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

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<MemberSignupResponse> signup(@Valid @RequestBody MemberSignupRequest request){
        return ResponseEntity.ok(signUpService.signup(request));
    }
}
