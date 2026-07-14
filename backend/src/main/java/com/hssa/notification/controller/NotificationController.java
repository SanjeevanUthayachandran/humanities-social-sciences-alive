package com.hssa.notification.controller;

import com.hssa.auth.entity.User;
import com.hssa.notification.dto.request.BroadcastNotificationRequest;
import com.hssa.notification.dto.request.CreateNotificationRequest;
import com.hssa.notification.dto.request.NotificationPreferenceRequest;
import com.hssa.notification.dto.response.NotificationDetailResponse;
import com.hssa.notification.dto.response.NotificationPreferenceResponse;
import com.hssa.notification.dto.response.NotificationStatisticsResponse;
import com.hssa.notification.dto.response.NotificationSummaryResponse;
import com.hssa.notification.service.NotificationPreferenceService;
import com.hssa.notification.service.NotificationService;
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
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationPreferenceService preferenceService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationSummaryResponse>> listNotifications(
            @AuthenticationPrincipal User user, Pageable pageable) {
        return ResponseEntity.ok(notificationService.listNotifications(user.getId(), pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationDetailResponse> getNotification(
            @PathVariable UUID id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getNotification(id, user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> sendNotification(
            @Valid @RequestBody CreateNotificationRequest request,
            @AuthenticationPrincipal User user) {
        notificationService.sendNotification(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> broadcastNotification(
            @Valid @RequestBody BroadcastNotificationRequest request,
            @AuthenticationPrincipal User user) {
        notificationService.broadcastNotification(request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(
            @PathVariable UUID id, @AuthenticationPrincipal User user) {
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal User user) {
        notificationService.markAllAsRead(user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable UUID id, @AuthenticationPrincipal User user) {
        notificationService.deleteNotification(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationSummaryResponse>> getHistory(
            @AuthenticationPrincipal User user, Pageable pageable) {
        // Simple alias for list for this requirement
        return ResponseEntity.ok(notificationService.listNotifications(user.getId(), pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<NotificationSummaryResponse>> searchNotifications(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(notificationService.searchNotifications(user.getId(), type, status, keyword, pageable));
    }

    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationStatisticsResponse> getStatistics(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(notificationService.getStatistics(user.getId()));
    }

    @GetMapping("/preferences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationPreferenceResponse> getPreferences(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(preferenceService.getPreferences(user.getId()));
    }

    @PutMapping("/preferences")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificationPreferenceResponse> updatePreferences(
            @AuthenticationPrincipal User user,
            @RequestBody NotificationPreferenceRequest request) {
        return ResponseEntity.ok(preferenceService.updatePreferences(user.getId(), request));
    }
}