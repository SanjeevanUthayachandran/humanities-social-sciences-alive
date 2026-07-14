package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemHealthResponse {
    private String status;
    private String databaseStatus;
    private String cacheStatus;
}