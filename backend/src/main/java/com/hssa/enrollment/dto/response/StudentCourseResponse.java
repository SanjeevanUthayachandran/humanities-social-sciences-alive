package com.hssa.enrollment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class StudentCourseResponse {
    private UUID enrollmentId;
    private UUID courseId;
    private String courseTitle;
    private String courseSlug;
    private String status;
    private LocalDateTime enrolledAt;
}
