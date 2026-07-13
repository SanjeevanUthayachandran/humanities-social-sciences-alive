package com.hssa.enrollment.service.impl;

import com.hssa.course.entity.Course;
import com.hssa.course.repository.CourseRepository;
import com.hssa.enrollment.dto.request.CreateEnrollmentRequest;
import com.hssa.enrollment.dto.request.SearchEnrollmentRequest;
import com.hssa.enrollment.dto.response.*;
import com.hssa.enrollment.entity.Enrollment;
import com.hssa.enrollment.enums.EnrollmentStatus;
import com.hssa.enrollment.event.*;
import com.hssa.enrollment.repository.EnrollmentRepository;
import com.hssa.enrollment.service.EnrollmentService;
import com.hssa.enrollment.specification.EnrollmentSpecification;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    private Course getCourse(UUID courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateCourseAccess(Course course, UUID userId, boolean isAdmin) {
        if (isAdmin) return;
        boolean isInstructor = course.getInstructors().stream()
                .anyMatch(ci -> ci.getInstructorId().equals(userId));
        if (!isInstructor && !course.getCreatedBy().equals(userId)) {
            throw new BusinessException("You do not have permission to manage enrollments for this course");
        }
    }

    @Override
    @Transactional
    public EnrollmentResponse enroll(CreateEnrollmentRequest request, UUID userId) {
        Course course = getCourse(request.getCourseId());
        User user = getUser(userId);

        if (!course.getStatus().name().equals("PUBLISHED")) {
            throw new BusinessException("Cannot enroll in an unpublished course");
        }

        if (enrollmentRepository.existsByUserIdAndCourseIdAndDeletedAtIsNull(userId, course.getId())) {
            throw new BusinessException("User is already enrolled in this course");
        }

        EnrollmentStatus status = course.getIsFree() ? EnrollmentStatus.ENROLLED : EnrollmentStatus.PENDING;

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .status(status)
                .enrolledAt(LocalDateTime.now())
                .build();

        enrollment = enrollmentRepository.save(enrollment);
        eventPublisher.publishEvent(new EnrollmentCreatedEvent(this, enrollment.getId()));

        return toEnrollmentResponse(enrollment);
    }

    @Override
    @Transactional
    public void withdraw(UUID enrollmentId, UUID userId) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (!enrollment.getUser().getId().equals(userId)) {
            throw new BusinessException("You can only withdraw from your own enrollments");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
        eventPublisher.publishEvent(new EnrollmentWithdrawnEvent(this, enrollment.getId()));
    }

    @Override
    @Transactional
    public void cancel(UUID enrollmentId, UUID userId, boolean isAdmin) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        validateCourseAccess(enrollment.getCourse(), userId, isAdmin);

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollment.setDeletedAt(LocalDateTime.now());
        enrollmentRepository.save(enrollment);
        eventPublisher.publishEvent(new EnrollmentCancelledEvent(this, enrollment.getId()));
    }

    @Override
    @Transactional
    public void approve(UUID enrollmentId, UUID userId, boolean isAdmin) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        validateCourseAccess(enrollment.getCourse(), userId, isAdmin);

        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new BusinessException("Can only approve PENDING enrollments");
        }

        enrollment.setStatus(EnrollmentStatus.ENROLLED);
        enrollmentRepository.save(enrollment);
        eventPublisher.publishEvent(new EnrollmentApprovedEvent(this, enrollment.getId()));
    }

    @Override
    @Transactional
    public void reject(UUID enrollmentId, UUID userId, boolean isAdmin) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        validateCourseAccess(enrollment.getCourse(), userId, isAdmin);

        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new BusinessException("Can only reject PENDING enrollments");
        }

        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);
        eventPublisher.publishEvent(new EnrollmentRejectedEvent(this, enrollment.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentDetailResponse getEnrollmentById(UUID id, UUID userId, boolean isAdmin) {
        Enrollment enrollment = enrollmentRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        if (!isAdmin && !enrollment.getUser().getId().equals(userId)) {
            validateCourseAccess(enrollment.getCourse(), userId, false); // Throws if not instructor
        }

        return toEnrollmentDetailResponse(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentSummaryResponse> searchEnrollments(SearchEnrollmentRequest request, Pageable pageable, UUID userId, boolean isAdmin) {
        if (!isAdmin) {
            // Further logic can be added to restrict instructors to only their courses
        }
        return enrollmentRepository.findAll(EnrollmentSpecification.filterBy(request), pageable)
                .map(this::toEnrollmentSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentCourseResponse> getStudentCourses(UUID studentId, Pageable pageable, UUID currentUserId, boolean isAdmin) {
        if (!isAdmin && !studentId.equals(currentUserId)) {
            throw new BusinessException("You can only view your own enrolled courses");
        }
        SearchEnrollmentRequest request = new SearchEnrollmentRequest();
        request.setUserId(studentId);
        
        return enrollmentRepository.findAll(EnrollmentSpecification.filterBy(request), pageable)
                .map(this::toStudentCourseResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseStudentResponse> getCourseStudents(UUID courseId, Pageable pageable, UUID currentUserId, boolean isAdmin) {
        Course course = getCourse(courseId);
        validateCourseAccess(course, currentUserId, isAdmin);

        SearchEnrollmentRequest request = new SearchEnrollmentRequest();
        request.setCourseId(courseId);
        
        return enrollmentRepository.findAll(EnrollmentSpecification.filterBy(request), pageable)
                .map(this::toCourseStudentResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EnrollmentStatisticsResponse getEnrollmentStatistics(UUID courseId, UUID currentUserId, boolean isAdmin) {
        Course course = getCourse(courseId);
        validateCourseAccess(course, currentUserId, isAdmin);

        long total = enrollmentRepository.findByCourseIdAndDeletedAtIsNull(courseId).size();
        long active = enrollmentRepository.countEnrolledStudents(courseId);
        long completed = enrollmentRepository.findByCourseIdAndDeletedAtIsNull(courseId).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.COMPLETED).count();
        long cancelled = enrollmentRepository.findByCourseIdAndDeletedAtIsNull(courseId).stream()
                .filter(e -> e.getStatus() == EnrollmentStatus.CANCELLED).count();

        return EnrollmentStatisticsResponse.builder()
                .courseId(courseId)
                .totalEnrolled(total)
                .activeEnrolled(active)
                .completed(completed)
                .cancelled(cancelled)
                .build();
    }

    private EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .courseId(enrollment.getCourse().getId())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .expiresAt(enrollment.getExpiresAt())
                .build();
    }

    private EnrollmentSummaryResponse toEnrollmentSummaryResponse(Enrollment enrollment) {
        return EnrollmentSummaryResponse.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .studentName(enrollment.getUser().getFirstName() + " " + enrollment.getUser().getLastName())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }

    private EnrollmentDetailResponse toEnrollmentDetailResponse(Enrollment enrollment) {
        return EnrollmentDetailResponse.builder()
                .id(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .studentName(enrollment.getUser().getFirstName() + " " + enrollment.getUser().getLastName())
                .studentEmail(enrollment.getUser().getEmail())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .expiresAt(enrollment.getExpiresAt())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .build();
    }

    private StudentCourseResponse toStudentCourseResponse(Enrollment enrollment) {
        return StudentCourseResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .courseSlug(enrollment.getCourse().getSlug())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }

    private CourseStudentResponse toCourseStudentResponse(Enrollment enrollment) {
        return CourseStudentResponse.builder()
                .enrollmentId(enrollment.getId())
                .userId(enrollment.getUser().getId())
                .studentName(enrollment.getUser().getFirstName() + " " + enrollment.getUser().getLastName())
                .studentEmail(enrollment.getUser().getEmail())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }
}
