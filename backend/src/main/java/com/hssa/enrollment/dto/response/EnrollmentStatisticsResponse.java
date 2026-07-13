package com.hssa.enrollment.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class EnrollmentStatisticsResponse {
    private UUID courseId;
    private long totalEnrolled;
    private long activeEnrolled;
    private long completed;
    private long cancelled;
}
