package com.hssa.enrollment.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SearchEnrollmentRequest {
    private UUID userId;
    private UUID courseId;
    private String status;
    private String keyword;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
