package com.hssa.literary.repository;

import com.hssa.literary.entity.LiteraryArticleTag;
import com.hssa.literary.entity.LiteraryArticleTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LiteraryArticleTagRepository extends JpaRepository<LiteraryArticleTag, LiteraryArticleTagId> {
    List<LiteraryArticleTag> findByArticleId(UUID articleId);
    void deleteByArticleId(UUID articleId);
}
