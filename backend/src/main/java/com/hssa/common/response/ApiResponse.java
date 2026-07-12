package com.hssa.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Common API Response structure for all REST endpoints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String path;
    private Integer status;
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .status(200)
                .build();
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation successful");
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .status(201)
                .build();
    }

    public static <T> ApiResponse<T> updated(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .status(200)
                .build();
    }

    public static <T> ApiResponse<T> deleted(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(null)
                .status(200)
                .build();
    }
    
    public static <T> ApiResponse<T> failure(String message, Integer status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .status(status)
                .path(path)
                .build();
    }
}
