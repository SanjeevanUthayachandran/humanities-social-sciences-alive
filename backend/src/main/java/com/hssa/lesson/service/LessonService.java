package com.hssa.lesson.service;

import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.lesson.dto.request.*;
import com.hssa.lesson.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LessonService {

    LessonResponse createLesson(CreateLessonRequest request, UUID userId, boolean isAdmin);
    LessonResponse updateLesson(UUID id, UpdateLessonRequest request, UUID userId, boolean isAdmin);
    void publishLesson(UUID id, boolean publish, UUID userId, boolean isAdmin);
    void deleteLesson(UUID id, UUID userId, boolean isAdmin);
    void reorderLessons(UUID moduleId, ReorderLessonRequest request, UUID userId, boolean isAdmin);
    
    LessonDetailResponse getLessonById(UUID id, UUID userId, boolean isAdmin);
    LessonDetailResponse getLessonBySlug(String slug, UUID userId, boolean isAdmin);
    
    Page<LessonSummaryResponse> searchLessons(SearchLessonRequest request, Pageable pageable, UUID userId, boolean isAdmin);

    LessonResourceResponse addResource(UUID lessonId, CreateLessonResourceRequest request, UUID userId, boolean isAdmin);
    void removeResource(UUID lessonId, UUID resourceId, UUID userId, boolean isAdmin);

    LessonVideoResponse setVideo(UUID lessonId, SetLessonVideoRequest request, UUID userId, boolean isAdmin);
    void removeVideo(UUID lessonId, UUID userId, boolean isAdmin);
}
