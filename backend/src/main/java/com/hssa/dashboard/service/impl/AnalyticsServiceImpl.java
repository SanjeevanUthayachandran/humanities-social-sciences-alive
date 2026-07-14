package com.hssa.dashboard.service.impl;

import com.hssa.auth.repository.UserRepository;
import com.hssa.dashboard.dto.response.RecentActivityResponse;
import com.hssa.dashboard.dto.response.RevenueResponse;
import com.hssa.dashboard.dto.response.StatisticsResponse;
import com.hssa.dashboard.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final JdbcTemplate jdbcTemplate; // Using JdbcTemplate for complex aggregate queries where existing repositories don't suffice

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'systemStats'")
    public StatisticsResponse getSystemStatistics() {
        return StatisticsResponse.builder()
                .category("SYSTEM")
                .metrics(Map.of(
                        "totalUsers", userRepository.count(),
                        "uptime", "99.9%"
                ))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'userStats'")
    public StatisticsResponse getUserStatistics() {
        return StatisticsResponse.builder()
                .category("USERS")
                .metrics(Map.of(
                        "totalUsers", userRepository.count()
                ))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'courseStats'")
    public StatisticsResponse getCourseStatistics() {
        Long courseCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lms.courses WHERE deleted_at IS NULL", Long.class);
        return StatisticsResponse.builder()
                .category("COURSES")
                .metrics(Map.of(
                        "totalCourses", courseCount != null ? courseCount : 0
                ))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'enrollmentStats'")
    public StatisticsResponse getEnrollmentStatistics() {
        Long enrollmentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM lms.enrollments", Long.class);
        return StatisticsResponse.builder()
                .category("ENROLLMENTS")
                .metrics(Map.of(
                        "totalEnrollments", enrollmentCount != null ? enrollmentCount : 0
                ))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardCache", key = "'revenueStats'")
    public RevenueResponse getRevenueStatistics() {
        BigDecimal total = jdbcTemplate.queryForObject("SELECT SUM(amount) FROM billing.payments WHERE payment_status = 'COMPLETED'", BigDecimal.class);
        return RevenueResponse.builder()
                .totalRevenue(total != null ? total : BigDecimal.ZERO)
                .thisMonthRevenue(BigDecimal.ZERO)
                .lastMonthRevenue(BigDecimal.ZERO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecentActivityResponse> getRecentActivities(int limit) {
        // Return mock data since Audit Log module doesn't exist explicitly for recent activities
        return Collections.emptyList();
    }
}