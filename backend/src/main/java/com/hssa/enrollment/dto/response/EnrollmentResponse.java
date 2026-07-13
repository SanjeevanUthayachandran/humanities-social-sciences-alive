package com.hssa.enrollment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EnrollmentResponse {
    private UUID id;
    private UUID userId;
    private UUID courseId;
    private String status;
    private LocalDateTime enrolledAt;
    private LocalDateTime expiresAt;
}
