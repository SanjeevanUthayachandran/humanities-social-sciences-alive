package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiteraryCommentRepository extends JpaRepository<LiteraryComment, UUID> {
    List<LiteraryComment> findByArticleIdAndParentCommentIsNullOrderByCreatedAtAsc(UUID articleId);
    List<LiteraryComment> findByParentCommentId(UUID parentCommentId);
}
