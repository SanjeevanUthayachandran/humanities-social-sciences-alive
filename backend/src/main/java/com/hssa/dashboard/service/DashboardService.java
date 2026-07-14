package com.hssa.dashboard.service;

import com.hssa.dashboard.dto.response.AdminDashboardResponse;
import com.hssa.dashboard.dto.response.InstructorDashboardResponse;
import com.hssa.dashboard.dto.response.PublicDashboardResponse;
import com.hssa.dashboard.dto.response.StudentDashboardResponse;

import java.util.UUID;

public interface DashboardService {
    AdminDashboardResponse getAdminDashboard();
    InstructorDashboardResponse getInstructorDashboard(UUID instructorId);
    StudentDashboardResponse getStudentDashboard(UUID studentId);
    PublicDashboardResponse getPublicDashboard();
}