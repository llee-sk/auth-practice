package com.example.auth_practice.global.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        boolean success,
        String message,
        int status,
        String code,
        T data
) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                HttpStatus.OK.value(),
                "SUCCESS",
                data
        );
    }
}
