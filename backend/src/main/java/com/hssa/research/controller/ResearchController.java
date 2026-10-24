package com.hssa.research.controller;

import com.hssa.research.dto.request.AddMemberRequest;
import com.hssa.research.dto.request.CreateResearchRequest;
import com.hssa.research.dto.request.UpdateResearchRequest;
import com.hssa.research.dto.response.ResearchDetailResponse;
import com.hssa.research.dto.response.ResearchMemberResponse;
import com.hssa.research.dto.response.ResearchSummaryResponse;
import com.hssa.research.service.ResearchService;
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
@RequestMapping("/api/v1/research")
@RequiredArgsConstructor
public class ResearchController {

    private final ResearchService researchService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ResearchDetailResponse> createResearch(
            @Valid @RequestBody CreateResearchRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(researchService.createResearch(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<ResearchDetailResponse> updateResearch(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateResearchRequest request) {
        return ResponseEntity.ok(researchService.updateResearch(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteResearch(@PathVariable UUID id) {
        researchService.deleteResearch(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> archiveResearch(@PathVariable UUID id) {
        researchService.archiveResearch(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> restoreResearch(@PathVariable UUID id) {
        researchService.restoreResearch(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResearchDetailResponse> getResearch(@PathVariable UUID id) {
        return ResponseEntity.ok(researchService.getResearch(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ResearchDetailResponse> getResearchBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(researchService.getResearchBySlug(slug));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResearchSummaryResponse>> searchResearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(researchService.searchResearch(title, category, status, keyword, pageable));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<ResearchMemberResponse>> getMembers(@PathVariable UUID id) {
        return ResponseEntity.ok(researchService.getMembers(id));
    }

    @PostMapping("/{id}/members")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> addMember(
            @PathVariable UUID id,
            @Valid @RequestBody AddMemberRequest request) {
        researchService.addMember(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members/{memberId}")
    @PreAuthorize("hasRole('USER') or hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID id,
            @PathVariable UUID memberId) {
        researchService.removeMember(id, memberId);
        return ResponseEntity.noContent().build();
    }
}
