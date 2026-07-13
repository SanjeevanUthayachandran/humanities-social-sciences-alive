package com.hssa.enrollment.service;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.enrollment.dto.request.CreateEnrollmentRequest;
import com.hssa.enrollment.dto.request.SearchEnrollmentRequest;
import com.hssa.enrollment.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnrollmentService {
    EnrollmentResponse enroll(CreateEnrollmentRequest request, UUID userId);
    void withdraw(UUID enrollmentId, UUID userId);
    void cancel(UUID enrollmentId, UUID userId, boolean isAdmin);
    void approve(UUID enrollmentId, UUID userId, boolean isAdmin);
    void reject(UUID enrollmentId, UUID userId, boolean isAdmin);
    
    EnrollmentDetailResponse getEnrollmentById(UUID id, UUID userId, boolean isAdmin);
    
    Page<EnrollmentSummaryResponse> searchEnrollments(SearchEnrollmentRequest request, Pageable pageable, UUID userId, boolean isAdmin);
    Page<StudentCourseResponse> getStudentCourses(UUID studentId, Pageable pageable, UUID currentUserId, boolean isAdmin);
    Page<CourseStudentResponse> getCourseStudents(UUID courseId, Pageable pageable, UUID currentUserId, boolean isAdmin);
    
    EnrollmentStatisticsResponse getEnrollmentStatistics(UUID courseId, UUID currentUserId, boolean isAdmin);
}
