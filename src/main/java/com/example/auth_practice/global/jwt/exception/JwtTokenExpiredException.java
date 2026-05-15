package com.example.auth_practice.global.jwt.exception;

public class JwtTokenExpiredException extends RuntimeException {
    public JwtTokenExpiredException(){
        super("JWT 토큰이 만료되었습니다.");
    }
}
