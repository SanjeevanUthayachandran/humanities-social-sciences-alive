package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InstructorDashboardResponse {
    private StatisticsResponse courseStats;
    private RevenueResponse revenue;
    private List<RecentActivityResponse> recentActivities;
}