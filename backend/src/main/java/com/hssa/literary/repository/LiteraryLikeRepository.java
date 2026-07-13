package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LiteraryLikeRepository extends JpaRepository<LiteraryLike, UUID> {
    boolean existsByArticleIdAndUserId(UUID articleId, UUID userId);
    Optional<LiteraryLike> findByArticleIdAndUserId(UUID articleId, UUID userId);
    long countByArticleId(UUID articleId);
}
