package com.hssa.dashboard.service.impl;

import com.hssa.dashboard.dto.response.AdminDashboardResponse;
import com.hssa.dashboard.dto.response.InstructorDashboardResponse;
import com.hssa.dashboard.dto.response.PublicDashboardResponse;
import com.hssa.dashboard.dto.response.StudentDashboardResponse;
import com.hssa.dashboard.service.AnalyticsService;
import com.hssa.dashboard.service.DashboardService;
import com.hssa.dashboard.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AnalyticsService analyticsService;
    private final ReportService reportService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getAdminDashboard() {
        return AdminDashboardResponse.builder()
                .systemStats(analyticsService.getSystemStatistics())
                .revenue(analyticsService.getRevenueStatistics())
                .recentActivities(analyticsService.getRecentActivities(10))
                .systemHealth(reportService.getSystemHealth())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorDashboardResponse getInstructorDashboard(UUID instructorId) {
        return InstructorDashboardResponse.builder()
                .courseStats(analyticsService.getCourseStatistics())
                .revenue(analyticsService.getRevenueStatistics())
                .recentActivities(analyticsService.getRecentActivities(5))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDashboardResponse getStudentDashboard(UUID studentId) {
        return StudentDashboardResponse.builder()
                .learningStats(analyticsService.getEnrollmentStatistics())
                .recentActivities(analyticsService.getRecentActivities(5))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'publicDashboard'")
    public PublicDashboardResponse getPublicDashboard() {
        Long totalCourses = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lms.courses WHERE deleted_at IS NULL", Long.class);
        Long totalStudents = jdbcTemplate.queryForObject("SELECT COUNT(DISTINCT user_id) FROM lms.enrollments", Long.class);
        Long totalEvents = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM event.events WHERE deleted_at IS NULL", Long.class);
        return PublicDashboardResponse.builder()
                .totalCourses(totalCourses != null ? totalCourses : 0)
                .totalStudents(totalStudents != null ? totalStudents : 0)
                .totalInstructors(0L)
                .totalEvents(totalEvents != null ? totalEvents : 0)
                .build();
    }
}