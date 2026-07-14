package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class StatisticsResponse {
    private String category;
    private Map<String, Object> metrics;
}