package com.hssa.course.controller;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.common.response.ApiResponse;
import com.hssa.course.dto.request.CreateCourseRequest;
import com.hssa.course.dto.request.PublishCourseRequest;
import com.hssa.course.dto.request.SearchCourseRequest;
import com.hssa.course.dto.request.UpdateCourseRequest;
import com.hssa.course.dto.response.CourseDetailResponse;
import com.hssa.course.dto.response.CourseResponse;
import com.hssa.course.dto.response.CourseSummaryResponse;
import com.hssa.course.service.CourseService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Course Management", description = "Endpoints for handling courses")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Create a new course", description = "Create a draft course. Requires ADMIN or INSTRUCTOR role.")
    public ResponseEntity<ApiResponse<CourseResponse>> createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        CourseResponse response = courseService.createCourse(request, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Course created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update a course", description = "Update course details. Requires owner or ADMIN role.")
    public ResponseEntity<ApiResponse<CourseResponse>> updateCourse(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCourseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        CourseResponse response = courseService.updateCourse(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Course updated successfully"));
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Publish/Unpublish course", description = "Toggle course published state")
    public ResponseEntity<ApiResponse<Void>> publishCourse(
            @PathVariable UUID id,
            @Valid @RequestBody PublishCourseRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        courseService.publishCourse(id, request, userDetails.getId(), isAdmin);
        String msg = request.isPublish() ? "Course published successfully" : "Course unpublished successfully";
        return ResponseEntity.ok(ApiResponse.success(null, msg));
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Archive course", description = "Archives a course instead of deleting it")
    public ResponseEntity<ApiResponse<Void>> archiveCourse(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        courseService.archiveCourse(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Course archived successfully"));
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Restore course", description = "Restores a soft-deleted course")
    public ResponseEntity<ApiResponse<Void>> restoreCourse(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        courseService.restoreCourse(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Course restored successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Delete course", description = "Soft deletes a course. Published courses cannot be deleted.")
    public ResponseEntity<ApiResponse<Void>> deleteCourse(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        courseService.deleteCourse(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Course soft-deleted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by ID", description = "Retrieves a course. Private/Unpublished courses require permission.")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseById(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        CourseDetailResponse response = courseService.getCourseById(id, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Course retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get course by slug", description = "Retrieves a course by its URL slug.")
    public ResponseEntity<ApiResponse<CourseDetailResponse>> getCourseBySlug(
            @PathVariable String slug,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        CourseDetailResponse response = courseService.getCourseBySlug(slug, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Course retrieved successfully"));
    }

    @PostMapping("/search")
    @Operation(summary = "Search courses", description = "Search and filter courses with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<CourseSummaryResponse>>> searchCourses(
            @RequestBody SearchCourseRequest request,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
            
        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        Page<CourseSummaryResponse> page = courseService.searchCourses(request, pageable, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Courses searched successfully"));
    }
}
