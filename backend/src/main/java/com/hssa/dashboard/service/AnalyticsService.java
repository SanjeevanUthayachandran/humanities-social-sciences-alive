package com.hssa.dashboard.service;

import com.hssa.dashboard.dto.response.RecentActivityResponse;
import com.hssa.dashboard.dto.response.RevenueResponse;
import com.hssa.dashboard.dto.response.StatisticsResponse;

import java.util.List;

public interface AnalyticsService {
    StatisticsResponse getSystemStatistics();
    StatisticsResponse getUserStatistics();
    StatisticsResponse getCourseStatistics();
    StatisticsResponse getEnrollmentStatistics();
    RevenueResponse getRevenueStatistics();
    List<RecentActivityResponse> getRecentActivities(int limit);
}