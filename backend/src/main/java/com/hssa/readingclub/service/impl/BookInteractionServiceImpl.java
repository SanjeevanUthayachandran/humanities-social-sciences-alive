package com.hssa.readingclub.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.readingclub.dto.request.CreateBookReviewRequest;
import com.hssa.readingclub.dto.response.BookReviewResponse;
import com.hssa.readingclub.entity.Book;
import com.hssa.readingclub.entity.BookBookmark;
import com.hssa.readingclub.entity.BookReview;
import com.hssa.readingclub.repository.BookBookmarkRepository;
import com.hssa.readingclub.repository.BookRepository;
import com.hssa.readingclub.repository.BookReviewRepository;
import com.hssa.readingclub.service.BookInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookInteractionServiceImpl implements BookInteractionService {

    private final BookReviewRepository bookReviewRepository;
    private final BookBookmarkRepository bookBookmarkRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookReviewResponse createReview(CreateBookReviewRequest request, UUID userId) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BookReview review = BookReview.builder()
                .book(book)
                .user(user)
                .rating(request.getRating())
                .review(request.getReview())
                .build();
        review = bookReviewRepository.save(review);
        return toReviewResponse(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookReviewResponse> getBookReviews(UUID bookId) {
        return bookReviewRepository.findByBookId(bookId).stream()
                .map(this::toReviewResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReview(UUID reviewId) {
        BookReview review = bookReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        bookReviewRepository.delete(review);
    }

    @Override
    @Transactional
    public void addBookmark(UUID bookId, UUID userId) {
        if (bookBookmarkRepository.existsByUserIdAndBookId(userId, bookId)) {
            throw new BusinessException("Book is already bookmarked");
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        BookBookmark bookmark = BookBookmark.builder()
                .book(book)
                .user(user)
                .build();
        bookBookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(UUID bookId, UUID userId) {
        bookBookmarkRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(UUID bookId, UUID userId) {
        return bookBookmarkRepository.existsByUserIdAndBookId(userId, bookId);
    }

    private BookReviewResponse toReviewResponse(BookReview review) {
        return BookReviewResponse.builder()
                .id(review.getId())
                .bookId(review.getBook().getId())
                .userId(review.getUser().getId())
                .userFullName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .rating(review.getRating())
                .review(review.getReview())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
