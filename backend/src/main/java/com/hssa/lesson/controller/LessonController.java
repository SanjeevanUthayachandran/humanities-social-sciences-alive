package com.hssa.lesson.controller;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.common.response.ApiResponse;
import com.hssa.lesson.dto.request.*;
import com.hssa.lesson.dto.response.*;
import com.hssa.lesson.service.LessonService;
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
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "Lesson Management", description = "Endpoints for managing course lessons")
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Create a lesson", description = "Create a new lesson")
    public ResponseEntity<ApiResponse<LessonResponse>> createLesson(
            @Valid @RequestBody CreateLessonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonResponse response = lessonService.createLesson(request, userDetails.getId(), isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Lesson created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Update a lesson", description = "Update an existing lesson")
    public ResponseEntity<ApiResponse<LessonResponse>> updateLesson(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLessonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonResponse response = lessonService.updateLesson(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Lesson updated successfully"));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Partial update lesson", description = "Partially update lesson details")
    public ResponseEntity<ApiResponse<LessonResponse>> partialUpdateLesson(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLessonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonResponse response = lessonService.updateLesson(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Lesson updated successfully"));
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Publish a lesson", description = "Publish a draft lesson")
    public ResponseEntity<ApiResponse<Void>> publishLesson(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.publishLesson(id, true, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Lesson published successfully"));
    }

    @PatchMapping("/{id}/unpublish")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Unpublish a lesson", description = "Unpublish an active lesson")
    public ResponseEntity<ApiResponse<Void>> unpublishLesson(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.publishLesson(id, false, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Lesson unpublished successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Delete a lesson", description = "Soft delete a lesson")
    public ResponseEntity<ApiResponse<Void>> deleteLesson(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.deleteLesson(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Lesson deleted successfully"));
    }

    @PatchMapping("/modules/{moduleId}/reorder")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Reorder lessons", description = "Reorder lessons within a module")
    public ResponseEntity<ApiResponse<Void>> reorderLessons(
            @PathVariable UUID moduleId,
            @Valid @RequestBody ReorderLessonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.reorderLessons(moduleId, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Lessons reordered successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lesson by ID", description = "Get detailed lesson information")
    public ResponseEntity<ApiResponse<LessonDetailResponse>> getLessonById(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonDetailResponse response = lessonService.getLessonById(id, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Lesson retrieved successfully"));
    }

    @PostMapping("/search")
    @Operation(summary = "Search lessons", description = "Search lessons with pagination")
    public ResponseEntity<ApiResponse<PaginationResponse<LessonSummaryResponse>>> searchLessons(
            @RequestBody SearchLessonRequest request,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<LessonSummaryResponse> page = lessonService.searchLessons(request, pageable, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Lessons retrieved successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search lessons via GET", description = "Search lessons via query params")
    public ResponseEntity<ApiResponse<PaginationResponse<LessonSummaryResponse>>> searchLessonsGet(
            @RequestParam(required = false) UUID courseId,
            @RequestParam(required = false) UUID moduleId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String lessonType,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Boolean isFreePreview,
            Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        SearchLessonRequest request = new SearchLessonRequest();
        request.setCourseId(courseId);
        request.setModuleId(moduleId);
        request.setKeyword(keyword);
        request.setLessonType(lessonType);
        request.setIsActive(isActive);
        request.setIsFreePreview(isFreePreview);

        UUID userId = userDetails != null ? userDetails.getId() : null;
        boolean isAdmin = userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<LessonSummaryResponse> page = lessonService.searchLessons(request, pageable, userId, isAdmin);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Lessons retrieved successfully"));
    }

    // Resources & Videos
    @PostMapping("/{id}/resources")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Add a resource", description = "Add a resource to a lesson")
    public ResponseEntity<ApiResponse<LessonResourceResponse>> addResource(
            @PathVariable UUID id,
            @Valid @RequestBody CreateLessonResourceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonResourceResponse response = lessonService.addResource(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Resource added successfully"));
    }

    @DeleteMapping("/{id}/resources/{resourceId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Remove a resource", description = "Remove a resource from a lesson")
    public ResponseEntity<ApiResponse<Void>> removeResource(
            @PathVariable UUID id,
            @PathVariable UUID resourceId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.removeResource(id, resourceId, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Resource removed successfully"));
    }

    @PostMapping("/{id}/video")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Set video", description = "Set or update the video for a lesson")
    public ResponseEntity<ApiResponse<LessonVideoResponse>> setVideo(
            @PathVariable UUID id,
            @Valid @RequestBody SetLessonVideoRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        LessonVideoResponse response = lessonService.setVideo(id, request, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Video set successfully"));
    }

    @DeleteMapping("/{id}/video")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
    @Operation(summary = "Remove video", description = "Remove the video from a lesson")
    public ResponseEntity<ApiResponse<Void>> removeVideo(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        lessonService.removeVideo(id, userDetails.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "Video removed successfully"));
    }
}
