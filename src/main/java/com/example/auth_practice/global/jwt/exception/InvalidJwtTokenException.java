package com.example.auth_practice.global.jwt.exception;

public class InvalidJwtTokenException extends RuntimeException{
    public InvalidJwtTokenException(){
        super("유효하지 않은 JWT 토큰 입니다.");
    }
}
