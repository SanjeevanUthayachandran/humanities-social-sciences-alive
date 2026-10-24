package com.hssa.payment.controller;

import com.hssa.course.entity.Course;
import com.hssa.course.repository.CourseRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.payment.dto.request.CoursePaymentSettingsRequest;
import com.hssa.payment.dto.response.CoursePaymentSettingsResponse;
import com.hssa.payment.entity.CoursePaymentSettings;
import com.hssa.payment.repository.CoursePaymentSettingsRepository;
import com.hssa.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/course-payments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCoursePaymentController {

    private final CourseRepository courseRepository;
    private final CoursePaymentSettingsRepository settingsRepository;

    @GetMapping("/{courseId}")
    public ResponseEntity<CoursePaymentSettingsResponse> getSettings(@PathVariable UUID courseId) {
        CoursePaymentSettings settings = settingsRepository.findByCourse_Id(courseId)
                .orElseGet(() -> defaultSettings(getCourse(courseId)));
        return ResponseEntity.ok(toResponse(settings));
    }

    @PutMapping("/{courseId}")
    @Transactional
    public ResponseEntity<CoursePaymentSettingsResponse> updateSettings(
            @PathVariable UUID courseId,
            @Valid @RequestBody CoursePaymentSettingsRequest request,
            @AuthenticationPrincipal CustomUserDetails admin) {
        Course course = getCourse(courseId);
        CoursePaymentSettings settings = settingsRepository.findByCourse_Id(courseId)
                .orElseGet(() -> defaultSettings(course));
        settings.setGatewayCode(request.getGatewayCode().trim().toUpperCase(Locale.ROOT));
        settings.setOnlinePaymentEnabled(request.isOnlinePaymentEnabled());
        settings.setBankTransferEnabled(request.isBankTransferEnabled());
        settings.setInstallmentsEnabled(request.isInstallmentsEnabled());
        settings.setUpdatedAt(LocalDateTime.now());
        settings.setUpdatedBy(admin.getId());
        return ResponseEntity.ok(toResponse(settingsRepository.save(settings)));
    }

    private Course getCourse(UUID courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
    }

    private CoursePaymentSettings defaultSettings(Course course) {
        return CoursePaymentSettings.builder()
                .course(course)
                .onlinePaymentEnabled(!Boolean.TRUE.equals(course.getIsFree()))
                .updatedAt(LocalDateTime.now())
                .version(0)
                .build();
    }

    private CoursePaymentSettingsResponse toResponse(CoursePaymentSettings settings) {
        return new CoursePaymentSettingsResponse(
                settings.getCourse().getId(),
                settings.getGatewayCode(),
                settings.isOnlinePaymentEnabled(),
                settings.isBankTransferEnabled(),
                settings.isInstallmentsEnabled());
    }
}
