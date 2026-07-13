package com.hssa.literary.controller;

import com.hssa.auth.entity.User;
import com.hssa.literary.dto.request.CommentRequest;
import com.hssa.literary.dto.response.LiteraryCommentResponse;
import com.hssa.literary.service.LiteraryInteractionService;
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
@RequestMapping("/api/v1/literary")
@RequiredArgsConstructor
public class LiteraryInteractionController {

    private final LiteraryInteractionService interactionService;

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> like(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        interactionService.likeArticle(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unlike(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        interactionService.unlikeArticle(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> bookmark(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        interactionService.bookmarkArticle(id, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> unbookmark(@PathVariable UUID id, @AuthenticationPrincipal User user) {
        interactionService.unbookmarkArticle(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<LiteraryCommentResponse> addComment(
            @PathVariable UUID id,
            @Valid @RequestBody CommentRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(interactionService.addComment(id, request, user.getId()));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<LiteraryCommentResponse>> getComments(@PathVariable UUID id) {
        return ResponseEntity.ok(interactionService.getComments(id));
    }
}
