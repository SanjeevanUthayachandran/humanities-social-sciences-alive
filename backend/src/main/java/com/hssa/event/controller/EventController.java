package com.hssa.event.controller;

import com.hssa.auth.entity.User;
import com.hssa.event.dto.request.CreateEventRequest;
import com.hssa.event.dto.request.UpdateEventRequest;
import com.hssa.event.dto.response.EventDetailResponse;
import com.hssa.event.dto.response.EventStatisticsResponse;
import com.hssa.event.dto.response.EventSummaryResponse;
import com.hssa.event.service.EventService;
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
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<EventSummaryResponse>> listEvents(Pageable pageable) {
        return ResponseEntity.ok(eventService.listEvents(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDetailResponse> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(eventService.getEvent(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<EventDetailResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createEvent(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<EventDetailResponse> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEventRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(eventService.updateEvent(id, request, user.getId()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<EventDetailResponse> patchEvent(
            @PathVariable UUID id,
            @RequestBody UpdateEventRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(eventService.updateEvent(id, request, user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN') or hasRole('EVENT_ORGANIZER')")
    public ResponseEntity<Void> publishEvent(@PathVariable UUID id) {
        eventService.publishEvent(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelEvent(@PathVariable UUID id) {
        eventService.cancelEvent(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> completeEvent(@PathVariable UUID id) {
        eventService.completeEvent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<EventSummaryResponse>> searchEvents(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String locationType,
            @RequestParam(required = false) Boolean isFree,
            Pageable pageable) {
        return ResponseEntity.ok(eventService.searchEvents(title, status, eventType, locationType, isFree, pageable));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<EventSummaryResponse>> getUpcoming(Pageable pageable) {
        return ResponseEntity.ok(eventService.getUpcoming(pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<EventSummaryResponse>> getFeatured(Pageable pageable) {
        return ResponseEntity.ok(eventService.getFeatured(pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<EventStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(eventService.getStatistics());
    }
}