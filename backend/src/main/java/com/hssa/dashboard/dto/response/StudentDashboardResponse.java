package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StudentDashboardResponse {
    private StatisticsResponse learningStats;
    private List<RecentActivityResponse> recentActivities;
}