package com.hssa.dashboard.controller;

import com.hssa.security.dto.CustomUserDetails;
import com.hssa.dashboard.dto.response.AdminDashboardResponse;
import com.hssa.dashboard.dto.response.InstructorDashboardResponse;
import com.hssa.dashboard.dto.response.PublicDashboardResponse;
import com.hssa.dashboard.dto.response.StudentDashboardResponse;
import com.hssa.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardResponse> getAdminDashboard() {
        return ResponseEntity.ok(dashboardService.getAdminDashboard());
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<InstructorDashboardResponse> getInstructorDashboard(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(dashboardService.getInstructorDashboard(user.getId()));
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StudentDashboardResponse> getStudentDashboard(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(dashboardService.getStudentDashboard(user.getId()));
    }

    @GetMapping("/public")
    public ResponseEntity<PublicDashboardResponse> getPublicDashboard() {
        return ResponseEntity.ok(dashboardService.getPublicDashboard());
    }
}
