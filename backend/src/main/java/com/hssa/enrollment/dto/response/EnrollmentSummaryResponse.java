package com.hssa.enrollment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EnrollmentSummaryResponse {
    private UUID id;
    private UUID userId;
    private String studentName;
    private UUID courseId;
    private String courseTitle;
    private String status;
    private LocalDateTime enrolledAt;
}
