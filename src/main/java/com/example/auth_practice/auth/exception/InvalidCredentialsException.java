package com.example.auth_practice.auth.exception;

import com.example.auth_practice.global.exception.BusinessException;
import com.example.auth_practice.global.exception.ErrorCode;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super(ErrorCode.INVALID_CREDENTIALS);
    }
}
