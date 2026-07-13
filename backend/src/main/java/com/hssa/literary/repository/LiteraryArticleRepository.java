package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface LiteraryArticleRepository extends JpaRepository<LiteraryArticle, UUID>, JpaSpecificationExecutor<LiteraryArticle> {
    Optional<LiteraryArticle> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsBySlugAndDeletedAtIsNull(String slug);
}
