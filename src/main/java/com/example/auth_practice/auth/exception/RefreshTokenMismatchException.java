package com.example.auth_practice.auth.exception;

import com.example.auth_practice.global.exception.BusinessException;
import com.example.auth_practice.global.exception.ErrorCode;

public class RefreshTokenMismatchException extends BusinessException {
    public RefreshTokenMismatchException() {
        super(ErrorCode.REFRESH_TOKEN_MISMATCH);
    }
}
