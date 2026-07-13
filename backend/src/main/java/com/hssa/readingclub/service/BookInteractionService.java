package com.hssa.readingclub.service;

import com.hssa.readingclub.dto.request.CreateBookReviewRequest;
import com.hssa.readingclub.dto.response.BookReviewResponse;

import java.util.List;
import java.util.UUID;

public interface BookInteractionService {
    BookReviewResponse createReview(CreateBookReviewRequest request, UUID userId);
    List<BookReviewResponse> getBookReviews(UUID bookId);
    void deleteReview(UUID reviewId);
    void addBookmark(UUID bookId, UUID userId);
    void removeBookmark(UUID bookId, UUID userId);
    boolean isBookmarked(UUID bookId, UUID userId);
}
