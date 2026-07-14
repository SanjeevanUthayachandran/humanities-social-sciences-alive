package com.hssa.dashboard.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicDashboardResponse {
    private long totalCourses;
    private long totalStudents;
    private long totalInstructors;
    private long totalEvents;
}