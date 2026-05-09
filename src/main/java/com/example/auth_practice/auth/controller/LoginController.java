package com.example.auth_practice.auth.controller;

import com.example.auth_practice.auth.dto.request.LoginRequest;
import com.example.auth_practice.auth.service.LoginService;
import com.example.auth_practice.auth.session.SessionConst;
import com.example.auth_practice.auth.session.SessionMember;
import com.example.auth_practice.global.dto.ApiResponse;
import com.example.auth_practice.member.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "로그인 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponse>> login(@Valid @RequestBody LoginRequest request,
                                                             HttpSession session){

        MemberResponse memberResponse = loginService.login(request);
        SessionMember sessionMember = new SessionMember(
                memberResponse.id(),
                memberResponse.email(),
                memberResponse.name(),
                memberResponse.role()
        );

        session.setAttribute(SessionConst.LOGIN_MEMBER, sessionMember);

        return ResponseEntity.ok(ApiResponse.success("로그인 검증에 성공했습니다.", memberResponse));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        if (session != null){
            session.invalidate();
        }

        return ResponseEntity.ok(ApiResponse.success("로그아웃에 성공했습니다.", null));
    }
}
