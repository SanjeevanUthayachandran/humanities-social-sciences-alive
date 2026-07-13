package com.hssa.readingclub.repository;

import com.hssa.readingclub.entity.BookBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BookBookmarkRepository extends JpaRepository<BookBookmark, UUID> {
    List<BookBookmark> findByUserId(UUID userId);
    void deleteByUserIdAndBookId(UUID userId, UUID bookId);
    boolean existsByUserIdAndBookId(UUID userId, UUID bookId);
}
