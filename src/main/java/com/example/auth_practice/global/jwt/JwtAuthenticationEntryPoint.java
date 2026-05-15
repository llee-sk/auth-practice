package com.example.auth_practice.global.jwt;

import com.example.auth_practice.global.exception.ErrorCode;
import com.example.auth_practice.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = getErrorCode(request);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .success(false)
                .message(errorCode.getMessage())
                .status(errorCode.getStatus().value())
                .code(errorCode.name())
                .build();

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private ErrorCode getErrorCode(HttpServletRequest request){
        Object exception = request.getAttribute("exception");

        if (exception instanceof ErrorCode errorCode) {
            return errorCode;
        }

        return ErrorCode.ACCESS_TOKEN_NOT_FOUND;
    }
}
