package com.hssa.workshop.controller;

import com.hssa.security.dto.CustomUserDetails;
import com.hssa.workshop.dto.request.CreateWorkshopRequest;
import com.hssa.workshop.dto.request.UpdateWorkshopRequest;
import com.hssa.workshop.dto.response.WorkshopDetailResponse;
import com.hssa.workshop.dto.response.WorkshopStatisticsResponse;
import com.hssa.workshop.dto.response.WorkshopSummaryResponse;
import com.hssa.workshop.service.WorkshopService;
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
@RequestMapping("/api/v1/workshops")
@RequiredArgsConstructor
public class WorkshopController {

    private final WorkshopService workshopService;

    @GetMapping
    public ResponseEntity<Page<WorkshopSummaryResponse>> listWorkshops(Pageable pageable) {
        return ResponseEntity.ok(workshopService.listWorkshops(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkshopDetailResponse> getWorkshop(@PathVariable UUID id) {
        return ResponseEntity.ok(workshopService.getWorkshop(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<WorkshopDetailResponse> createWorkshop(
            @Valid @RequestBody CreateWorkshopRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(workshopService.createWorkshop(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<WorkshopDetailResponse> updateWorkshop(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWorkshopRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(workshopService.updateWorkshop(id, request, user.getId()));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<WorkshopDetailResponse> patchWorkshop(
            @PathVariable UUID id,
            @RequestBody UpdateWorkshopRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(workshopService.updateWorkshop(id, request, user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable UUID id) {
        workshopService.deleteWorkshop(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> publishWorkshop(@PathVariable UUID id) {
        workshopService.publishWorkshop(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cancelWorkshop(@PathVariable UUID id) {
        workshopService.cancelWorkshop(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> completeWorkshop(@PathVariable UUID id) {
        workshopService.completeWorkshop(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<WorkshopSummaryResponse>> searchWorkshops(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String locationType,
            @RequestParam(required = false) Boolean isFree,
            Pageable pageable) {
        return ResponseEntity.ok(workshopService.searchWorkshops(title, status, eventType, locationType, isFree, pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<WorkshopSummaryResponse>> getFeatured(Pageable pageable) {
        return ResponseEntity.ok(workshopService.getFeatured(pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<WorkshopStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(workshopService.getStatistics());
    }
}
