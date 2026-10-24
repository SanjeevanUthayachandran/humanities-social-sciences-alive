package com.hssa.course.service.impl;

import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.course.dto.request.CreateCourseRequest;
import com.hssa.course.dto.request.PublishCourseRequest;
import com.hssa.course.dto.request.SearchCourseRequest;
import com.hssa.course.dto.request.UpdateCourseRequest;
import com.hssa.course.dto.response.CourseDetailResponse;
import com.hssa.course.dto.response.CourseResponse;
import com.hssa.course.dto.response.CourseSummaryResponse;
import com.hssa.course.entity.Course;
import com.hssa.course.entity.CourseCategory;
import com.hssa.course.entity.CourseInstructor;
import com.hssa.course.entity.CourseTag;
import com.hssa.course.enums.CourseLevel;
import com.hssa.course.enums.CourseStatus;
import com.hssa.course.enums.CourseVisibility;
import com.hssa.course.event.*;
import com.hssa.course.repository.CourseCategoryRepository;
import com.hssa.course.repository.CourseInstructorRepository;
import com.hssa.course.repository.CourseRepository;
import com.hssa.course.repository.CourseTagRepository;
import com.hssa.course.service.CourseService;
import com.hssa.course.specification.CourseSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseCategoryRepository categoryRepository;
    private final CourseTagRepository tagRepository;
    private final CourseInstructorRepository instructorRepository;
    private final ApplicationEventPublisher eventPublisher;

    private Course getActiveCourse(UUID id) {
        return courseRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private void validateModificationAccess(Course course, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        boolean isInstructor = course.getInstructors().stream()
                .anyMatch(ci -> ci.getInstructorId().equals(userId));
        if (!isInstructor && !course.getCreatedBy().equals(userId)) {
            throw new BusinessException("You do not have permission to modify this course");
        }
    }

    private String generateSlug(String title) {
        String baseSlug = title.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("(^-|-$)", "");
        String slug = baseSlug;
        int count = 1;
        while (courseRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + count++;
        }
        return slug;
    }

    @Override
    @Transactional
    public CourseResponse createCourse(CreateCourseRequest request, UUID userId) {
        if (courseRepository.existsByTitle(request.getTitle())) {
            throw new BusinessException("Course title already exists");
        }

        CourseCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                .orElseThrow(() -> new BusinessException("Invalid category ID"));

        Course course = Course.builder()
                .category(category)
                .title(request.getTitle())
                .slug(generateSlug(request.getTitle()))
                .subtitle(request.getSubtitle())
                .description(request.getDescription())
                .language(request.getLanguage() != null ? request.getLanguage() : "English")
                .level(request.getLevel() != null ? CourseLevel.valueOf(request.getLevel()) : CourseLevel.BEGINNER)
                .visibility(request.getVisibility() != null ? CourseVisibility.valueOf(request.getVisibility()) : CourseVisibility.PUBLIC)
                .isFree(request.getIsFree() != null ? request.getIsFree() : false)
                .price(request.getPrice())
                .currency(request.getCurrency() != null ? request.getCurrency() : "LKR")
                .discountedPrice(request.getDiscountedPrice())
                .certificationAvailable(request.getCertificationAvailable() != null ? request.getCertificationAvailable() : true)
                .thumbnailMediaId(request.getThumbnailMediaId())
                .bannerMediaId(request.getBannerMediaId())
                .promotionalVideoId(request.getPromotionalVideoId())
                .cardImageUrl(request.getCardImageUrl())
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .status(CourseStatus.DRAFT)
                .build();

        if (request.getTags() != null) {
            Set<CourseTag> tags = new HashSet<>();
            for (String tagName : request.getTags()) {
                CourseTag tag = tagRepository.findByTagName(tagName)
                        .orElseGet(() -> tagRepository.save(CourseTag.builder().tagName(tagName).build()));
                tags.add(tag);
            }
            course.setTags(tags);
        }

        course = courseRepository.save(course);

        if (request.getInstructorIds() != null) {
            for (int i = 0; i < request.getInstructorIds().size(); i++) {
                UUID instructorId = request.getInstructorIds().get(i);
                CourseInstructor ci = CourseInstructor.builder()
                        .course(course)
                        .instructorId(instructorId)
                        .isPrimary(i == 0)
                        .displayOrder(i)
                        .build();
                course.addInstructor(ci);
            }
        } else {
            // Self assign
            CourseInstructor ci = CourseInstructor.builder()
                    .course(course)
                    .instructorId(userId)
                    .isPrimary(true)
                    .displayOrder(0)
                    .build();
            course.addInstructor(ci);
        }

        courseRepository.save(course);

        eventPublisher.publishEvent(new CourseCreatedEvent(this, course.getId()));

        return toCourseResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(UUID id, UpdateCourseRequest request, UUID userId, boolean isAdmin) {
        Course course = getActiveCourse(id);
        validateModificationAccess(course, userId, isAdmin);

        if (request.getTitle() != null && !course.getTitle().equals(request.getTitle())) {
            if (courseRepository.existsByTitle(request.getTitle())) {
                throw new BusinessException("Course title already exists");
            }
            course.setTitle(request.getTitle());
            course.setSlug(generateSlug(request.getTitle()));
        }

        if (request.getCategoryId() != null) {
            CourseCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                    .orElseThrow(() -> new BusinessException("Invalid category ID"));
            course.setCategory(category);
        }

        if (request.getSubtitle() != null) course.setSubtitle(request.getSubtitle());
        if (request.getDescription() != null) course.setDescription(request.getDescription());
        if (request.getLanguage() != null) course.setLanguage(request.getLanguage());
        if (request.getLevel() != null) course.setLevel(CourseLevel.valueOf(request.getLevel()));
        if (request.getVisibility() != null) course.setVisibility(CourseVisibility.valueOf(request.getVisibility()));
        
        if (request.getIsFree() != null) course.setIsFree(request.getIsFree());
        if (request.getPrice() != null) course.setPrice(request.getPrice());
        if (request.getCurrency() != null) course.setCurrency(request.getCurrency());
        if (request.getDiscountedPrice() != null) course.setDiscountedPrice(request.getDiscountedPrice());
        if (request.getDurationHours() != null) course.setDurationHours(request.getDurationHours());
        if (request.getCertificationAvailable() != null) course.setCertificationAvailable(request.getCertificationAvailable());
        
        if (request.getThumbnailMediaId() != null) course.setThumbnailMediaId(request.getThumbnailMediaId());
        if (request.getBannerMediaId() != null) course.setBannerMediaId(request.getBannerMediaId());
        if (request.getPromotionalVideoId() != null) course.setPromotionalVideoId(request.getPromotionalVideoId());
        if (request.getCardImageUrl() != null) course.setCardImageUrl(request.getCardImageUrl());
        
        if (request.getMetaTitle() != null) course.setMetaTitle(request.getMetaTitle());
        if (request.getMetaDescription() != null) course.setMetaDescription(request.getMetaDescription());
        if (request.getMetaKeywords() != null) course.setMetaKeywords(request.getMetaKeywords());

        if (request.getTags() != null) {
            Set<CourseTag> tags = new HashSet<>();
            for (String tagName : request.getTags()) {
                CourseTag tag = tagRepository.findByTagName(tagName)
                        .orElseGet(() -> tagRepository.save(CourseTag.builder().tagName(tagName).build()));
                tags.add(tag);
            }
            course.setTags(tags);
        }

        if (request.getInstructorIds() != null) {
            course.getInstructors().clear();
            for (int i = 0; i < request.getInstructorIds().size(); i++) {
                UUID instructorId = request.getInstructorIds().get(i);
                CourseInstructor ci = CourseInstructor.builder()
                        .course(course)
                        .instructorId(instructorId)
                        .isPrimary(i == 0)
                        .displayOrder(i)
                        .build();
                course.addInstructor(ci);
            }
        }

        course = courseRepository.save(course);
        eventPublisher.publishEvent(new CourseUpdatedEvent(this, course.getId()));

        return toCourseResponse(course);
    }

    @Override
    @Transactional
    public void publishCourse(UUID id, PublishCourseRequest request, UUID userId, boolean isAdmin) {
        Course course = getActiveCourse(id);
        validateModificationAccess(course, userId, isAdmin);

        if (request.isPublish()) {
            if (course.getStatus() == CourseStatus.PUBLISHED) {
                throw new BusinessException("Course is already published");
            }
            course.setStatus(CourseStatus.PUBLISHED);
            course.setPublishedAt(LocalDateTime.now());
            courseRepository.save(course);
            eventPublisher.publishEvent(new CoursePublishedEvent(this, course.getId()));
        } else {
            course.setStatus(CourseStatus.DRAFT);
            courseRepository.save(course);
        }
    }

    @Override
    @Transactional
    public void archiveCourse(UUID id, UUID userId, boolean isAdmin) {
        Course course = getActiveCourse(id);
        validateModificationAccess(course, userId, isAdmin);

        course.setStatus(CourseStatus.ARCHIVED);
        courseRepository.save(course);
        eventPublisher.publishEvent(new CourseArchivedEvent(this, course.getId()));
    }

    @Override
    @Transactional
    public void restoreCourse(UUID id, UUID userId, boolean isAdmin) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        validateModificationAccess(course, userId, isAdmin);

        course.setDeletedAt(null);
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID id, UUID userId, boolean isAdmin) {
        Course course = getActiveCourse(id);
        validateModificationAccess(course, userId, isAdmin);

        if (course.getStatus() == CourseStatus.PUBLISHED) {
            throw new BusinessException("Published courses cannot be deleted. Archive it instead or unpublish first.");
        }

        course.setDeletedAt(LocalDateTime.now());
        courseRepository.save(course);
        eventPublisher.publishEvent(new CourseDeletedEvent(this, course.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseBySlug(String slug, UUID userId, boolean isAdmin) {
        Course course = courseRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        
        validateReadAccess(course, userId, isAdmin);
        return toCourseDetailResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(UUID id, UUID userId, boolean isAdmin) {
        Course course = getActiveCourse(id);
        validateReadAccess(course, userId, isAdmin);
        return toCourseDetailResponse(course);
    }

    private void validateReadAccess(Course course, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        if (course.getStatus() != CourseStatus.PUBLISHED || course.getVisibility() == CourseVisibility.PRIVATE) {
            if (userId == null) {
                throw new BusinessException("Unauthorized to view this course");
            }
            boolean isInstructor = course.getInstructors().stream()
                    .anyMatch(ci -> ci.getInstructorId().equals(userId));
            if (!isInstructor && !course.getCreatedBy().equals(userId)) {
                throw new BusinessException("You do not have permission to view this course");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSummaryResponse> searchCourses(SearchCourseRequest request, Pageable pageable, UUID userId, boolean isAdmin) {
        // Enforce published check if not admin
        if (!isAdmin) {
            request.setStatus(CourseStatus.PUBLISHED.name());
            request.setVisibility(CourseVisibility.PUBLIC.name());
        }
        
        return courseRepository.findAll(CourseSpecification.filterBy(request), pageable)
                .map(this::toCourseSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseSummaryResponse> getInstructorCourses(UUID instructorId) {
        return instructorRepository.findAllByInstructorId(instructorId).stream()
                .map(CourseInstructor::getCourse)
                .filter(course -> course.getDeletedAt() == null)
                .distinct()
                .map(this::toCourseSummaryResponse)
                .toList();
    }

    private CourseResponse toCourseResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .categoryId(course.getCategory() != null ? course.getCategory().getId() : null)
                .title(course.getTitle())
                .slug(course.getSlug())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .language(course.getLanguage())
                .level(course.getLevel().name())
                .status(course.getStatus().name())
                .visibility(course.getVisibility().name())
                .thumbnailMediaId(course.getThumbnailMediaId())
                .bannerMediaId(course.getBannerMediaId())
                .promotionalVideoId(course.getPromotionalVideoId())
                .cardImageUrl(course.getCardImageUrl())
                .isFree(course.getIsFree())
                .price(course.getPrice())
                .currency(course.getCurrency())
                .discountedPrice(course.getDiscountedPrice())
                .durationHours(course.getDurationHours())
                .certificationAvailable(course.getCertificationAvailable())
                .publishedAt(course.getPublishedAt())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .tags(course.getTags().stream().map(CourseTag::getTagName).collect(Collectors.toList()))
                .instructorIds(course.getInstructors().stream().map(CourseInstructor::getInstructorId).collect(Collectors.toList()))
                .build();
    }

    private CourseSummaryResponse toCourseSummaryResponse(Course course) {
        return CourseSummaryResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)
                .level(course.getLevel().name())
                .status(course.getStatus().name())
                .thumbnailMediaId(course.getThumbnailMediaId())
                .cardImageUrl(course.getCardImageUrl())
                .isFree(course.getIsFree())
                .price(course.getPrice())
                .discountedPrice(course.getDiscountedPrice())
                .currency(course.getCurrency())
                .durationHours(course.getDurationHours())
                .tags(course.getTags().stream().map(CourseTag::getTagName).collect(Collectors.toList()))
                .build();
    }

    private CourseDetailResponse toCourseDetailResponse(Course course) {
        return CourseDetailResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .subtitle(course.getSubtitle())
                .description(course.getDescription())
                .language(course.getLanguage())
                .level(course.getLevel().name())
                .categoryName(course.getCategory() != null ? course.getCategory().getName() : null)
                .thumbnailMediaId(course.getThumbnailMediaId())
                .bannerMediaId(course.getBannerMediaId())
                .promotionalVideoId(course.getPromotionalVideoId())
                .cardImageUrl(course.getCardImageUrl())
                .isFree(course.getIsFree())
                .price(course.getPrice())
                .discountedPrice(course.getDiscountedPrice())
                .currency(course.getCurrency())
                .durationHours(course.getDurationHours())
                .certificationAvailable(course.getCertificationAvailable())
                .publishedAt(course.getPublishedAt())
                .tags(course.getTags().stream().map(CourseTag::getTagName).collect(Collectors.toList()))
                .instructorIds(course.getInstructors().stream().map(CourseInstructor::getInstructorId).collect(Collectors.toList()))
                .build();
    }
}
