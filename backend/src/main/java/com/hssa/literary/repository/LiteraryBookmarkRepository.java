package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LiteraryBookmarkRepository extends JpaRepository<LiteraryBookmark, UUID> {
    boolean existsByArticleIdAndUserId(UUID articleId, UUID userId);
    Optional<LiteraryBookmark> findByArticleIdAndUserId(UUID articleId, UUID userId);
    List<LiteraryBookmark> findByUserId(UUID userId);
}
