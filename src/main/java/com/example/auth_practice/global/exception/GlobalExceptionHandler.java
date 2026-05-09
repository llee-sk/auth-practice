package com.example.auth_practice.global.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Validation exception occurred", ex);
        ErrorCode errorCode = ErrorCode.INVALID_REQUEST;
        String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(message)
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .build();

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Unexpected exception occurred", ex);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse response = getErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    private ErrorResponse getErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .success(false)
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .build();
    }
}
