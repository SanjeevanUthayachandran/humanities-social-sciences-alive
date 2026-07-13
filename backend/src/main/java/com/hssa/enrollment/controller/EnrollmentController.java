package com.hssa.enrollment.controller;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.common.response.ApiResponse;
import com.hssa.enrollment.dto.request.CreateEnrollmentRequest;
import com.hssa.enrollment.dto.request.SearchEnrollmentRequest;
import com.hssa.enrollment.dto.response.*;
import com.hssa.enrollment.service.EnrollmentService;
import com.hssa.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Enrollment Management", description = "Endpoints for handling user enrollments in courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enrollments")
    @Operation(summary = "Enroll in a course", description = "Allows a user to enroll in a published course")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @Valid @RequestBody CreateEnrollmentRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        EnrollmentResponse response = enrollmentService.enroll(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Enrolled successfully"));
    }

    @DeleteMapping("/enrollments/{id}")
    @Operation(summary = "Cancel enrollment", description = "Allows a user or admin to cancel an enrollment")
    public ResponseEntity<ApiResponse<Void>> cancelEnrollment(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        enrollmentService.cancel(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Enrollment cancelled successfully"));
    }

    @PatchMapping("/enrollments/{id}/withdraw")
    @Operation(summary = "Withdraw from course", description = "Allows a student to withdraw from their enrollment")
    public ResponseEntity<ApiResponse<Void>> withdrawEnrollment(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        enrollmentService.withdraw(id, userDetails.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Withdrawn from course successfully"));
    }

    @PatchMapping("/enrollments/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Approve enrollment", description = "Approve a pending enrollment")
    public ResponseEntity<ApiResponse<Void>> approveEnrollment(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        enrollmentService.approve(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Enrollment approved successfully"));
    }

    @PatchMapping("/enrollments/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Reject enrollment", description = "Reject a pending enrollment")
    public ResponseEntity<ApiResponse<Void>> rejectEnrollment(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        enrollmentService.reject(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Enrollment rejected successfully"));
    }

    @GetMapping("/enrollments/{id}")
    @Operation(summary = "Get enrollment details", description = "Get detailed information about an enrollment")
    public ResponseEntity<ApiResponse<EnrollmentDetailResponse>> getEnrollmentById(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        EnrollmentDetailResponse response = enrollmentService.getEnrollmentById(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Enrollment retrieved successfully"));
    }

    @GetMapping("/enrollments")
    @Operation(summary = "Search enrollments via GET", description = "Search enrollments with query params")
    public ResponseEntity<ApiResponse<PaginationResponse<EnrollmentSummaryResponse>>> searchEnrollmentsGet(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID courseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SearchEnrollmentRequest request = new SearchEnrollmentRequest();
        request.setUserId(userId);
        request.setCourseId(courseId);
        request.setStatus(status);
        request.setKeyword(keyword);
        request.setFromDate(fromDate);
        request.setToDate(toDate);

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<EnrollmentSummaryResponse> page = enrollmentService.searchEnrollments(request, pageable, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Enrollments retrieved successfully"));
    }

    @PostMapping("/enrollments/search")
    @Operation(summary = "Search enrollments", description = "Search enrollments with payload")
    public ResponseEntity<ApiResponse<PaginationResponse<EnrollmentSummaryResponse>>> searchEnrollments(
            @RequestBody SearchEnrollmentRequest request,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<EnrollmentSummaryResponse> page = enrollmentService.searchEnrollments(request, pageable, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Enrollments retrieved successfully"));
    }

    @GetMapping("/users/{userId}/courses")
    @Operation(summary = "Get student courses", description = "Get all courses a student is enrolled in")
    public ResponseEntity<ApiResponse<PaginationResponse<StudentCourseResponse>>> getStudentCourses(
            @PathVariable UUID userId,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<StudentCourseResponse> page = enrollmentService.getStudentCourses(userId, pageable, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Student courses retrieved successfully"));
    }

    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get course students", description = "Get all students enrolled in a course")
    public ResponseEntity<ApiResponse<PaginationResponse<CourseStudentResponse>>> getCourseStudents(
            @PathVariable UUID courseId,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<CourseStudentResponse> page = enrollmentService.getCourseStudents(courseId, pageable, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Course students retrieved successfully"));
    }

    @GetMapping("/courses/{courseId}/enrollments/statistics")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Get enrollment statistics", description = "Get enrollment statistics for a course")
    public ResponseEntity<ApiResponse<EnrollmentStatisticsResponse>> getEnrollmentStatistics(
            @PathVariable UUID courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        EnrollmentStatisticsResponse response = enrollmentService.getEnrollmentStatistics(courseId, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Enrollment statistics retrieved successfully"));
    }
}
