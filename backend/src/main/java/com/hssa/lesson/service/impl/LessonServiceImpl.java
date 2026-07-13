package com.hssa.lesson.service.impl;

import com.hssa.course.entity.Course;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.lesson.dto.request.*;
import com.hssa.lesson.dto.response.*;
import com.hssa.lesson.entity.Lesson;
import com.hssa.lesson.entity.LessonResource;
import com.hssa.lesson.entity.LessonVideo;
import com.hssa.lesson.entity.Module;
import com.hssa.lesson.enums.LessonType;
import com.hssa.lesson.enums.ResourceType;
import com.hssa.lesson.event.*;
import com.hssa.lesson.repository.LessonRepository;
import com.hssa.lesson.repository.LessonResourceRepository;
import com.hssa.lesson.repository.LessonVideoRepository;
import com.hssa.lesson.repository.ModuleRepository;
import com.hssa.lesson.service.LessonService;
import com.hssa.lesson.specification.LessonSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final LessonResourceRepository resourceRepository;
    private final LessonVideoRepository videoRepository;
    private final ApplicationEventPublisher eventPublisher;

    private void validateAccess(Course course, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        boolean isInstructor = course.getInstructors().stream()
                .anyMatch(ci -> ci.getInstructorId().equals(userId));
        if (!isInstructor && !course.getCreatedBy().equals(userId)) {
            throw new BusinessException("You do not have permission to modify lessons in this course");
        }
    }

    private String generateSlug(String title) {
        String baseSlug = title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        String slug = baseSlug;
        int count = 1;
        while (lessonRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + count++;
        }
        return slug;
    }

    @Override
    @Transactional
    public LessonResponse createLesson(CreateLessonRequest request, UUID userId, boolean isAdmin) {
        Module module = moduleRepository.findByIdAndDeletedAtIsNull(request.getModuleId())
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        validateAccess(module.getCourse(), userId, isAdmin);

        long count = lessonRepository.findByModuleIdAndDeletedAtIsNullOrderByDisplayOrderAsc(module.getId()).size();

        Lesson lesson = Lesson.builder()
                .module(module)
                .title(request.getTitle())
                .slug(generateSlug(request.getTitle()))
                .description(request.getDescription())
                .content(request.getContent())
                .lessonType(LessonType.valueOf(request.getLessonType()))
                .durationMinutes(request.getDurationMinutes())
                .isFreePreview(request.getIsFreePreview() != null ? request.getIsFreePreview() : false)
                .displayOrder((int) count)
                .isActive(false) // default draft
                .build();

        lesson = lessonRepository.save(lesson);
        eventPublisher.publishEvent(new LessonCreatedEvent(this, lesson.getId()));
        return toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public LessonResponse updateLesson(UUID id, UpdateLessonRequest request, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        if (request.getTitle() != null && !request.getTitle().equals(lesson.getTitle())) {
            lesson.setTitle(request.getTitle());
            lesson.setSlug(generateSlug(request.getTitle()));
        }

        if (request.getDescription() != null) lesson.setDescription(request.getDescription());
        if (request.getContent() != null) lesson.setContent(request.getContent());
        if (request.getLessonType() != null) lesson.setLessonType(LessonType.valueOf(request.getLessonType()));
        if (request.getDurationMinutes() != null) lesson.setDurationMinutes(request.getDurationMinutes());
        if (request.getIsFreePreview() != null) lesson.setIsFreePreview(request.getIsFreePreview());

        lesson = lessonRepository.save(lesson);
        eventPublisher.publishEvent(new LessonUpdatedEvent(this, lesson.getId()));
        return toLessonResponse(lesson);
    }

    @Override
    @Transactional
    public void publishLesson(UUID id, boolean publish, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        lesson.setIsActive(publish);
        lessonRepository.save(lesson);
        
        if (publish) {
            eventPublisher.publishEvent(new LessonPublishedEvent(this, lesson.getId()));
        }
    }

    @Override
    @Transactional
    public void deleteLesson(UUID id, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        lesson.setDeletedAt(LocalDateTime.now());
        lessonRepository.save(lesson);
        eventPublisher.publishEvent(new LessonDeletedEvent(this, lesson.getId()));
    }

    @Override
    @Transactional
    public void reorderLessons(UUID moduleId, ReorderLessonRequest request, UUID userId, boolean isAdmin) {
        Module module = moduleRepository.findByIdAndDeletedAtIsNull(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found"));
        validateAccess(module.getCourse(), userId, isAdmin);

        List<Lesson> lessons = lessonRepository.findByModuleIdAndDeletedAtIsNullOrderByDisplayOrderAsc(moduleId);
        for (int i = 0; i < request.getOrderedLessonIds().size(); i++) {
            UUID id = request.getOrderedLessonIds().get(i);
            lessons.stream().filter(l -> l.getId().equals(id)).findFirst().ifPresent(l -> {
                l.setDisplayOrder(request.getOrderedLessonIds().indexOf(id));
                lessonRepository.save(l);
            });
        }
        eventPublisher.publishEvent(new LessonReorderedEvent(this, moduleId));
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonById(UUID id, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateReadAccess(lesson, userId, isAdmin);
        return toLessonDetailResponse(lesson);
    }

    @Override
    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonBySlug(String slug, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateReadAccess(lesson, userId, isAdmin);
        return toLessonDetailResponse(lesson);
    }

    private void validateReadAccess(Lesson lesson, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        Course course = lesson.getModule().getCourse();
        boolean isInstructor = course.getInstructors().stream()
                .anyMatch(ci -> ci.getInstructorId().equals(userId));
        if (isInstructor || course.getCreatedBy().equals(userId)) return;

        if (!lesson.getIsActive() || (!lesson.getIsFreePreview() && !course.getIsFree() /* Simplified access check. Enrollment check is skipped as per prompt */)) {
            if (!lesson.getIsFreePreview()) {
                // Future: Enrollment check here
                throw new BusinessException("You must be enrolled to view this lesson");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LessonSummaryResponse> searchLessons(SearchLessonRequest request, Pageable pageable, UUID userId, boolean isAdmin) {
        if (!isAdmin) {
            request.setIsActive(true);
        }
        return lessonRepository.findAll(LessonSpecification.filterBy(request), pageable)
                .map(this::toLessonSummaryResponse);
    }

    @Override
    @Transactional
    public LessonResourceResponse addResource(UUID lessonId, CreateLessonResourceRequest request, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        long count = resourceRepository.findByLessonIdAndDeletedAtIsNullOrderByDisplayOrderAsc(lessonId).size();

        LessonResource resource = LessonResource.builder()
                .lesson(lesson)
                .title(request.getTitle())
                .resourceType(ResourceType.valueOf(request.getResourceType()))
                .mediaId(request.getMediaId())
                .externalLink(request.getExternalLink())
                .isDownloadable(request.getIsDownloadable() != null ? request.getIsDownloadable() : true)
                .displayOrder((int) count)
                .isActive(true)
                .build();

        resource = resourceRepository.save(resource);
        return toLessonResourceResponse(resource);
    }

    @Override
    @Transactional
    public void removeResource(UUID lessonId, UUID resourceId, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        LessonResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        
        resource.setDeletedAt(LocalDateTime.now());
        resourceRepository.save(resource);
    }

    @Override
    @Transactional
    public LessonVideoResponse setVideo(UUID lessonId, SetLessonVideoRequest request, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        LessonVideo video = videoRepository.findByLessonId(lessonId)
                .orElse(LessonVideo.builder().lesson(lesson).build());

        video.setMediaId(request.getMediaId());
        video.setDurationSeconds(request.getDurationSeconds());
        video.setIsDownloadable(request.getIsDownloadable() != null ? request.getIsDownloadable() : false);

        video = videoRepository.save(video);
        return toLessonVideoResponse(video);
    }

    @Override
    @Transactional
    public void removeVideo(UUID lessonId, UUID userId, boolean isAdmin) {
        Lesson lesson = lessonRepository.findByIdAndDeletedAtIsNull(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found"));
        validateAccess(lesson.getModule().getCourse(), userId, isAdmin);

        videoRepository.findByLessonId(lessonId).ifPresent(videoRepository::delete);
    }

    private LessonResponse toLessonResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .moduleId(lesson.getModule().getId())
                .title(lesson.getTitle())
                .slug(lesson.getSlug())
                .description(lesson.getDescription())
                .lessonType(lesson.getLessonType().name())
                .durationMinutes(lesson.getDurationMinutes())
                .isFreePreview(lesson.getIsFreePreview())
                .displayOrder(lesson.getDisplayOrder())
                .isActive(lesson.getIsActive())
                .build();
    }

    private LessonSummaryResponse toLessonSummaryResponse(Lesson lesson) {
        return LessonSummaryResponse.builder()
                .id(lesson.getId())
                .moduleId(lesson.getModule().getId())
                .title(lesson.getTitle())
                .slug(lesson.getSlug())
                .lessonType(lesson.getLessonType().name())
                .durationMinutes(lesson.getDurationMinutes())
                .isFreePreview(lesson.getIsFreePreview())
                .displayOrder(lesson.getDisplayOrder())
                .isActive(lesson.getIsActive())
                .build();
    }

    private LessonDetailResponse toLessonDetailResponse(Lesson lesson) {
        List<LessonResourceResponse> resources = resourceRepository.findByLessonIdAndDeletedAtIsNullOrderByDisplayOrderAsc(lesson.getId())
                .stream().map(this::toLessonResourceResponse).collect(Collectors.toList());
        
        LessonVideoResponse video = videoRepository.findByLessonId(lesson.getId())
                .map(this::toLessonVideoResponse).orElse(null);

        return LessonDetailResponse.builder()
                .id(lesson.getId())
                .moduleId(lesson.getModule().getId())
                .moduleTitle(lesson.getModule().getTitle())
                .courseId(lesson.getModule().getCourse().getId())
                .title(lesson.getTitle())
                .slug(lesson.getSlug())
                .description(lesson.getDescription())
                .content(lesson.getContent())
                .lessonType(lesson.getLessonType().name())
                .durationMinutes(lesson.getDurationMinutes())
                .isFreePreview(lesson.getIsFreePreview())
                .displayOrder(lesson.getDisplayOrder())
                .isActive(lesson.getIsActive())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .video(video)
                .resources(resources)
                .build();
    }

    private LessonResourceResponse toLessonResourceResponse(LessonResource resource) {
        return LessonResourceResponse.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .resourceType(resource.getResourceType().name())
                .mediaId(resource.getMediaId())
                .externalLink(resource.getExternalLink())
                .isDownloadable(resource.getIsDownloadable())
                .displayOrder(resource.getDisplayOrder())
                .build();
    }

    private LessonVideoResponse toLessonVideoResponse(LessonVideo video) {
        return LessonVideoResponse.builder()
                .id(video.getId())
                .mediaId(video.getMediaId())
                .durationSeconds(video.getDurationSeconds())
                .isDownloadable(video.getIsDownloadable())
                .build();
    }
}
