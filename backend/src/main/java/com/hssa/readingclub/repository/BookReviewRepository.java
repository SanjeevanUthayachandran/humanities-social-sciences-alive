package com.hssa.readingclub.repository;

import com.hssa.readingclub.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookReviewRepository extends JpaRepository<BookReview, UUID> {
    List<BookReview> findByBookId(UUID bookId);
}
