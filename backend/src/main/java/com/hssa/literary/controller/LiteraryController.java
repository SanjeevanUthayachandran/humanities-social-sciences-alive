package com.hssa.literary.controller;

import com.hssa.auth.entity.User;
import com.hssa.literary.dto.request.CreateLiteraryWorkRequest;
import com.hssa.literary.dto.request.UpdateLiteraryWorkRequest;
import com.hssa.literary.dto.response.LiteraryCategoryResponse;
import com.hssa.literary.dto.response.LiteraryWorkDetailResponse;
import com.hssa.literary.dto.response.LiteraryWorkSummaryResponse;
import com.hssa.literary.service.LiteraryService;
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
@RequestMapping("/api/v1/literary")
@RequiredArgsConstructor
public class LiteraryController {

    private final LiteraryService literaryService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LiteraryWorkDetailResponse> createWork(
            @Valid @RequestBody CreateLiteraryWorkRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(literaryService.createWork(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LiteraryWorkDetailResponse> updateWork(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLiteraryWorkRequest request) {
        return ResponseEntity.ok(literaryService.updateWork(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<Void> deleteWork(@PathVariable UUID id) {
        literaryService.deleteWork(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> publishWork(@PathVariable UUID id) {
        literaryService.publishWork(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR') or isAuthenticated()")
    public ResponseEntity<Void> archiveWork(@PathVariable UUID id) {
        literaryService.archiveWork(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LiteraryWorkDetailResponse> getWork(@PathVariable UUID id) {
        return ResponseEntity.ok(literaryService.getWork(id));
    }

    @GetMapping
    public ResponseEntity<Page<LiteraryWorkSummaryResponse>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String articleType,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID authorId,
            @RequestParam(required = false) Boolean isFeatured,
            Pageable pageable) {
        return ResponseEntity.ok(literaryService.search(title, status, articleType, categoryId, authorId, isFeatured, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<LiteraryWorkSummaryResponse>> searchWorks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String articleType,
            Pageable pageable) {
        return ResponseEntity.ok(literaryService.search(title, status, articleType, null, null, null, pageable));
    }

    @GetMapping("/featured")
    public ResponseEntity<Page<LiteraryWorkSummaryResponse>> getFeatured(Pageable pageable) {
        return ResponseEntity.ok(literaryService.getFeatured(pageable));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<LiteraryCategoryResponse>> listCategories() {
        return ResponseEntity.ok(literaryService.listCategories());
    }
}
