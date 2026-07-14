package com.hssa.event.controller;

import com.hssa.auth.entity.User;
import com.hssa.event.dto.request.EventAttendanceRequest;
import com.hssa.event.dto.request.EventFeedbackRequest;
import com.hssa.event.dto.response.EventRegistrationResponse;
import com.hssa.event.service.EventFeedbackService;
import com.hssa.event.service.EventRegistrationService;
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
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventRegistrationController {

    private final EventRegistrationService registrationService;
    private final EventFeedbackService feedbackService;

    @PostMapping("/{id}/register")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<EventRegistrationResponse> register(
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
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<Void> approveRegistration(
            @PathVariable UUID id,
            @RequestParam UUID userId) {
        registrationService.approveRegistration(id, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/attendance")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<Void> recordAttendance(
            @PathVariable UUID id,
            @Valid @RequestBody EventAttendanceRequest request,
            @AuthenticationPrincipal User user) {
        registrationService.recordAttendance(id, request, user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/registrations")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<List<EventRegistrationResponse>> getRegistrations(@PathVariable UUID id) {
        return ResponseEntity.ok(registrationService.getRegistrations(id));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> submitFeedback(
            @PathVariable UUID id,
            @Valid @RequestBody EventFeedbackRequest request,
            @AuthenticationPrincipal User user) {
        feedbackService.submitFeedback(id, request, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}