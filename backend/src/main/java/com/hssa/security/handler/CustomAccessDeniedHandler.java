package com.hssa.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hssa.common.response.ApiResponse;
import com.hssa.exception.constants.ExceptionConstants;
import com.hssa.exception.dto.ErrorResponse;
import com.hssa.exception.enums.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access denied error: {}", accessDeniedException.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.FORBIDDEN.value())
                .error(ErrorCode.SECURITY_002.name())
                .message(ExceptionConstants.FORBIDDEN)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .traceId(UUID.randomUUID().toString())
                .build();

        ApiResponse<ErrorResponse> apiResponse = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message(errorResponse.getMessage())
                .data(errorResponse)
                .status(HttpStatus.FORBIDDEN.value())
                .path(request.getRequestURI())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
