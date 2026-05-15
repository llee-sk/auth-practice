package com.example.auth_practice.auth.exception;

import com.example.auth_practice.global.exception.BusinessException;
import com.example.auth_practice.global.exception.ErrorCode;

public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException() {
        super(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
