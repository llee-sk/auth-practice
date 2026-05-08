package com.example.auth_practice.global.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private boolean success;
    private String message;
    private int status;
    private String code;
}
