package com.hssa.course.service;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.course.dto.request.CreateCourseRequest;
import com.hssa.course.dto.request.PublishCourseRequest;
import com.hssa.course.dto.request.SearchCourseRequest;
import com.hssa.course.dto.request.UpdateCourseRequest;
import com.hssa.course.dto.response.CourseDetailResponse;
import com.hssa.course.dto.response.CourseResponse;
import com.hssa.course.dto.response.CourseSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;
import java.util.List;

public interface CourseService {

    CourseResponse createCourse(CreateCourseRequest request, UUID userId);

    CourseResponse updateCourse(UUID id, UpdateCourseRequest request, UUID userId, boolean isAdmin);

    void publishCourse(UUID id, PublishCourseRequest request, UUID userId, boolean isAdmin);

    void archiveCourse(UUID id, UUID userId, boolean isAdmin);

    void restoreCourse(UUID id, UUID userId, boolean isAdmin);

    void deleteCourse(UUID id, UUID userId, boolean isAdmin);

    CourseDetailResponse getCourseBySlug(String slug, UUID userId, boolean isAdmin);

    CourseDetailResponse getCourseById(UUID id, UUID userId, boolean isAdmin);

    Page<CourseSummaryResponse> searchCourses(SearchCourseRequest request, Pageable pageable, UUID userId, boolean isAdmin);

    List<CourseSummaryResponse> getInstructorCourses(UUID instructorId);
}
