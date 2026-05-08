package com.example.auth_practice.auth.exception;

public class DuplicationEmailException extends RuntimeException{
    public DuplicationEmailException(){
        super("이미 사용 중인 이메일 입니다.");
    }
}