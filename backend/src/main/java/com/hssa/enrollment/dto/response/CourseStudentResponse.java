package com.hssa.enrollment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CourseStudentResponse {
    private UUID enrollmentId;
    private UUID userId;
    private String studentName;
    private String studentEmail;
    private String status;
    private LocalDateTime enrolledAt;
}
