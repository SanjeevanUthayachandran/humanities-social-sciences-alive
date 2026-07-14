package com.hssa.dashboard.service.impl;

import com.hssa.dashboard.dto.response.ChartResponse;
import com.hssa.dashboard.dto.response.ReportResponse;
import com.hssa.dashboard.dto.response.SystemHealthResponse;
import com.hssa.dashboard.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final JdbcTemplate jdbcTemplate;

    @Override
    @Cacheable(value = "dashboardCache", key = "'systemHealth'")
    public SystemHealthResponse getSystemHealth() {
        String dbStatus = "UP";
        try {
            jdbcTemplate.execute("SELECT 1");
        } catch (Exception e) {
            dbStatus = "DOWN";
        }
        return SystemHealthResponse.builder()
                .status(dbStatus.equals("UP") ? "UP" : "DOWN")
                .databaseStatus(dbStatus)
                .cacheStatus("UP")
                .build();
    }

    @Override
    @Cacheable(value = "dashboardCache", key = "#chartType + '-' + #range")
    public ChartResponse getChartsData(String chartType, String range) {
        return ChartResponse.builder()
                .chartType(chartType)
                .labels(List.of("Jan", "Feb", "Mar", "Apr", "May"))
                .data(List.of(10, 20, 30, 40, 50))
                .build();
    }

    @Override
    public ReportResponse getReports(String reportType) {
        return ReportResponse.builder()
                .reportType(reportType)
                .generatedAt(LocalDateTime.now())
                .summary(Map.of("status", "Generated successfully"))
                .build();
    }
}