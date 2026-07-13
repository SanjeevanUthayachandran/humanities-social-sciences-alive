package com.hssa.research.repository;

import com.hssa.research.entity.ResearchProjectKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResearchProjectKeywordRepository extends JpaRepository<ResearchProjectKeyword, UUID> {
    List<ResearchProjectKeyword> findByProjectId(UUID projectId);
    void deleteByProjectIdAndKeywordId(UUID projectId, UUID keywordId);
}
