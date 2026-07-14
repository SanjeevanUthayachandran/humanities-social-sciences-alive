package com.hssa.dashboard.controller;

import com.hssa.dashboard.dto.response.ChartResponse;
import com.hssa.dashboard.dto.response.RecentActivityResponse;
import com.hssa.dashboard.dto.response.ReportResponse;
import com.hssa.dashboard.dto.response.RevenueResponse;
import com.hssa.dashboard.dto.response.StatisticsResponse;
import com.hssa.dashboard.dto.response.SystemHealthResponse;
import com.hssa.dashboard.service.AnalyticsService;
import com.hssa.dashboard.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final ReportService reportService;

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StatisticsResponse> getStatistics(@RequestParam String category) {
        return switch (category.toUpperCase()) {
            case "SYSTEM" -> ResponseEntity.ok(analyticsService.getSystemStatistics());
            case "USERS" -> ResponseEntity.ok(analyticsService.getUserStatistics());
            case "COURSES" -> ResponseEntity.ok(analyticsService.getCourseStatistics());
            case "ENROLLMENTS" -> ResponseEntity.ok(analyticsService.getEnrollmentStatistics());
            default -> ResponseEntity.badRequest().build();
        };
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RevenueResponse> getRevenue() {
        return ResponseEntity.ok(analyticsService.getRevenueStatistics());
    }

    @GetMapping("/recent-activities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecentActivityResponse>> getRecentActivities(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getRecentActivities(limit));
    }

    @GetMapping("/system-health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemHealthResponse> getSystemHealth() {
        return ResponseEntity.ok(reportService.getSystemHealth());
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportResponse> getReports(@RequestParam String reportType) {
        return ResponseEntity.ok(reportService.getReports(reportType));
    }

    @GetMapping("/charts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ChartResponse> getChartsData(
            @RequestParam String chartType,
            @RequestParam(required = false, defaultValue = "1M") String range) {
        return ResponseEntity.ok(reportService.getChartsData(chartType, range));
    }
}