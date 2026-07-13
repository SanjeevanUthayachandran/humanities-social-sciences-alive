package com.hssa.readingclub.controller;

import com.hssa.auth.entity.User;
import com.hssa.readingclub.dto.request.CreateBookReviewRequest;
import com.hssa.readingclub.dto.response.BookReviewResponse;
import com.hssa.readingclub.service.BookInteractionService;
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
@RequestMapping("/api/v1/book-interactions")
@RequiredArgsConstructor
public class BookInteractionController {

    private final BookInteractionService bookInteractionService;

    @PostMapping("/reviews")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BookReviewResponse> createReview(
            @Valid @RequestBody CreateBookReviewRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookInteractionService.createReview(request, user.getId()));
    }

    @GetMapping("/reviews/book/{bookId}")
    public ResponseEntity<List<BookReviewResponse>> getBookReviews(@PathVariable UUID bookId) {
        return ResponseEntity.ok(bookInteractionService.getBookReviews(bookId));
    }

    @DeleteMapping("/reviews/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
        bookInteractionService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bookmarks/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> addBookmark(
            @PathVariable UUID bookId,
            @AuthenticationPrincipal User user) {
        bookInteractionService.addBookmark(bookId, user.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bookmarks/{bookId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> removeBookmark(
            @PathVariable UUID bookId,
            @AuthenticationPrincipal User user) {
        bookInteractionService.removeBookmark(bookId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bookmarks/{bookId}/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Boolean> isBookmarked(
            @PathVariable UUID bookId,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookInteractionService.isBookmarked(bookId, user.getId()));
    }
}
