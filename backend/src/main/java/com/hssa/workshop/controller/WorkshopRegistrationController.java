package com.hssa.workshop.controller;

import com.hssa.auth.entity.User;
import com.hssa.workshop.dto.request.AttendanceRequest;
import com.hssa.workshop.dto.request.WorkshopFeedbackRequest;
import com.hssa.workshop.dto.response.WorkshopRegistrationResponse;
import com.hssa.workshop.service.WorkshopFeedbackService;
import com.hssa.workshop.service.WorkshopRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/workshops")
@RequiredArgsConstructor
public class WorkshopRegistrationController {

    private final WorkshopRegistrationService registrationService;
    private final WorkshopFeedbackService feedbackService;

    @PostMapping("/{id}/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkshopRegistrationResponse> register(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registrationService.register(id, user.getId()));
    }

    @DeleteMapping("/{id}/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cancelRegistration(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user) {
        registrationService.cancelRegistration(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> approveRegistration(
            @PathVariable UUID id,
            @RequestParam UUID userId) {
        registrationService.approveRegistration(id, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> recordAttendance(
            @PathVariable UUID id,
            @Valid @RequestBody AttendanceRequest request,
            @AuthenticationPrincipal User user) {
        registrationService.recordAttendance(id, request, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/registrations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<List<WorkshopRegistrationResponse>> getRegistrations(@PathVariable UUID id) {
        return ResponseEntity.ok(registrationService.getRegistrations(id));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> submitFeedback(
            @PathVariable UUID id,
            @Valid @RequestBody WorkshopFeedbackRequest request,
            @AuthenticationPrincipal User user) {
        feedbackService.submitFeedback(id, request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
