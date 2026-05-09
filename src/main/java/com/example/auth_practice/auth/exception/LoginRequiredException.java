package com.example.auth_practice.auth.exception;

import com.example.auth_practice.global.exception.BusinessException;
import com.example.auth_practice.global.exception.ErrorCode;

public class LoginRequiredException extends BusinessException {
    public LoginRequiredException() {
        super(ErrorCode.LOGIN_REQUIRED);
    }
}
