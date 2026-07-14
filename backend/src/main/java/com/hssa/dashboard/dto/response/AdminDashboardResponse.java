package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdminDashboardResponse {
    private StatisticsResponse systemStats;
    private RevenueResponse revenue;
    private List<RecentActivityResponse> recentActivities;
    private SystemHealthResponse systemHealth;
}