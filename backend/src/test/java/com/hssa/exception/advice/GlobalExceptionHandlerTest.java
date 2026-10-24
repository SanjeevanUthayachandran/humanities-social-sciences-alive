package com.hssa.exception.advice;

import com.hssa.common.response.ApiResponse;
import com.hssa.exception.EmailDeliveryException;
import com.hssa.exception.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void badCredentialsReturnAUsefulLoginMessage() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/login");

        ResponseEntity<ApiResponse<ErrorResponse>> response = handler.handleBadCredentialsException(
                new BadCredentialsException("Bad credentials"),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid email or password.");
        assertThat(response.getBody().getData().getMessage()).isEqualTo("Invalid email or password.");
    }

    @Test
    void emailDeliveryFailureReturnsServiceUnavailable() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/auth/forgot-password");
        String message = "Password reset email is temporarily unavailable. Please contact HSSA support.";

        ResponseEntity<ApiResponse<ErrorResponse>> response = handler.handleEmailDeliveryException(
                new EmailDeliveryException(message),
                request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo(message);
        assertThat(response.getBody().getData().getMessage()).isEqualTo(message);
    }
}
