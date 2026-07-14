package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ReportResponse {
    private String reportType;
    private LocalDateTime generatedAt;
    private Map<String, Object> summary;
}