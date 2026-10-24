package com.hssa.publication.controller;

import com.hssa.publication.dto.request.AddAuthorRequest;
import com.hssa.publication.dto.request.CreatePublicationRequest;
import com.hssa.publication.dto.request.UpdatePublicationRequest;
import com.hssa.publication.dto.response.PublicationDetailResponse;
import com.hssa.publication.dto.response.PublicationAuthorResponse;
import com.hssa.publication.dto.response.PublicationSummaryResponse;
import com.hssa.publication.dto.response.PublicationStatisticsResponse;
import com.hssa.publication.service.PublicationService;
import com.hssa.security.dto.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/publications")
@RequiredArgsConstructor
public class PublicationController {

    private final PublicationService publicationService;

    @GetMapping
    public ResponseEntity<Page<PublicationSummaryResponse>> listPublications(Pageable pageable) {
        return ResponseEntity.ok(publicationService.listPublications(pageable));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PublicationDetailResponse> createPublication(
            @Valid @RequestBody CreatePublicationRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(publicationService.createPublication(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PublicationDetailResponse> updatePublication(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePublicationRequest request) {
        return ResponseEntity.ok(publicationService.updatePublication(id, request));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<PublicationDetailResponse> patchPublication(
            @PathVariable UUID id,
            @RequestBody UpdatePublicationRequest request) {
        return ResponseEntity.ok(publicationService.updatePublication(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePublication(@PathVariable UUID id) {
        publicationService.deletePublication(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> archivePublication(@PathVariable UUID id) {
        publicationService.archivePublication(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restorePublication(@PathVariable UUID id) {
        publicationService.restorePublication(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDetailResponse> getPublication(@PathVariable UUID id) {
        return ResponseEntity.ok(publicationService.getPublication(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PublicationSummaryResponse>> searchPublication(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String doi,
            Pageable pageable) {
        return ResponseEntity.ok(publicationService.searchPublication(title, category, status, doi, pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<PublicationStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(publicationService.getStatistics());
    }

    @GetMapping("/{id}/authors")
    public ResponseEntity<List<PublicationAuthorResponse>> getAuthors(@PathVariable UUID id) {
        return ResponseEntity.ok(publicationService.getAuthors(id));
    }

    @PostMapping("/{id}/authors")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> addAuthor(
            @PathVariable UUID id,
            @Valid @RequestBody AddAuthorRequest request) {
        publicationService.addAuthor(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/authors/{authorId}")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeAuthor(
            @PathVariable UUID id,
            @PathVariable UUID authorId) {
        publicationService.removeAuthor(id, authorId);
        return ResponseEntity.noContent().build();
    }
}
