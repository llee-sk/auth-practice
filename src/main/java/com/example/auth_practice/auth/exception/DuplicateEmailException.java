package com.example.auth_practice.auth.exception;

import com.example.auth_practice.global.exception.BusinessException;
import com.example.auth_practice.global.exception.ErrorCode;

public class DuplicateEmailException extends BusinessException {
    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}