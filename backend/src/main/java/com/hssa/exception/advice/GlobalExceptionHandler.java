package com.hssa.exception.advice;

import com.hssa.common.response.ApiResponse;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ConflictException;
import com.hssa.exception.EmailDeliveryException;
import com.hssa.exception.ForbiddenException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.exception.UnauthorizedException;
import com.hssa.exception.ValidationException;
import com.hssa.exception.constants.ExceptionConstants;
import com.hssa.exception.dto.ErrorResponse;
import com.hssa.exception.dto.ValidationError;
import com.hssa.exception.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        logError(ex, request);
        List<ValidationError> validationErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.add(ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .rejectedValue(fieldError.getRejectedValue())
                    .build());
        }
        
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, 
                ErrorCode.VALIDATION_001.name(), 
                ExceptionConstants.VALIDATION_FAILED, 
                request, 
                validationErrors);
        
        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        logError(ex, request);
        List<ValidationError> validationErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            validationErrors.add(ValidationError.builder()
                    .field(violation.getPropertyPath().toString())
                    .message(violation.getMessage())
                    .rejectedValue(violation.getInvalidValue())
                    .build());
        }

        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, 
                ErrorCode.VALIDATION_001.name(), 
                ExceptionConstants.VALIDATION_FAILED, 
                request, 
                validationErrors);
                
        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(ValidationException ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, 
                ErrorCode.VALIDATION_001.name(), 
                ex.getMessage(), 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MissingPathVariableException.class,
            TypeMismatchException.class
    })
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBadRequestExceptions(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.BAD_REQUEST, 
                ErrorCode.COMMON_001.name(), 
                ExceptionConstants.BAD_REQUEST, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ResourceNotFoundException.class, NoHandlerFoundException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFoundException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        String message = ex instanceof ResourceNotFoundException ? ex.getMessage() : ExceptionConstants.RESOURCE_NOT_FOUND;
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.NOT_FOUND, 
                ErrorCode.COMMON_002.name(), 
                message, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED, 
                ErrorCode.COMMON_001.name(), 
                ExceptionConstants.METHOD_NOT_ALLOWED, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.METHOD_NOT_ALLOWED, request);
    }

    @ExceptionHandler({AccessDeniedException.class, ForbiddenException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.FORBIDDEN, 
                ErrorCode.SECURITY_002.name(), 
                ExceptionConstants.FORBIDDEN, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({AuthenticationException.class, UnauthorizedException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthenticationException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.UNAUTHORIZED, 
                ErrorCode.SECURITY_001.name(), 
                ExceptionConstants.UNAUTHORIZED, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.SECURITY_001.name(),
                ExceptionConstants.INVALID_CREDENTIALS,
                request,
                null);

        return buildResponseEntity(errorResponse, HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataIntegrityViolationException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        String message = ex instanceof ConflictException ? ex.getMessage() : ExceptionConstants.DATA_INTEGRITY_VIOLATION;
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.CONFLICT, 
                ErrorCode.DATABASE_001.name(), 
                message, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.CONFLICT, request);
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE, 
                ErrorCode.COMMON_001.name(), 
                ExceptionConstants.PAYLOAD_TOO_LARGE, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.PAYLOAD_TOO_LARGE, request);
    }

    @ExceptionHandler(EmailDeliveryException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleEmailDeliveryException(EmailDeliveryException ex, HttpServletRequest request) {
        logError(ex, request);
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                ErrorCode.COMMON_001.name(),
                ex.getMessage(),
                request,
                null);

        return buildResponseEntity(errorResponse, HttpStatus.SERVICE_UNAVAILABLE, request);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class, BusinessException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGlobalException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        String message = ex instanceof BusinessException ? ex.getMessage() : ExceptionConstants.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, 
                ErrorCode.COMMON_001.name(), 
                message, 
                request, 
                null);
                
        return buildResponseEntity(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
    
    private ResponseEntity<ApiResponse<ErrorResponse>> buildResponseEntity(ErrorResponse errorResponse, HttpStatus status, HttpServletRequest request) {
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .success(false)
                .message(errorResponse.getMessage())
                .data(errorResponse)
                .status(status.value())
                .path(request.getRequestURI())
                .build();
        return new ResponseEntity<>(response, status);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String error, String message, HttpServletRequest request, List<ValidationError> validationErrors) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .method(request.getMethod())
                .traceId(UUID.randomUUID().toString())
                .validationErrors(validationErrors)
                .build();
    }

    private void logError(Exception ex, HttpServletRequest request) {
        log.error("Exception occurred. URI: {}, Method: {}, Type: {}, Message: {}", 
                request.getRequestURI(), 
                request.getMethod(), 
                ex.getClass().getName(), 
                ex.getMessage());
        
        if (!(ex instanceof MethodArgumentNotValidException) && 
            !(ex instanceof ResourceNotFoundException) && 
            !(ex instanceof ValidationException)) {
            log.debug("Exception details:", ex);
        }
    }
}
