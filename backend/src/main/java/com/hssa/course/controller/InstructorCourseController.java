package com.hssa.course.controller;

import com.hssa.common.response.ApiResponse;
import com.hssa.course.dto.response.CourseSummaryResponse;
import com.hssa.course.service.CourseService;
import com.hssa.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/instructor/courses")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'INSTRUCTOR')")
public class InstructorCourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseSummaryResponse>>> getAssignedCourses(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(
                courseService.getInstructorCourses(user.getId()), "Assigned courses retrieved"));
    }
}
