package com.hssa.research.repository;

import com.hssa.research.entity.ResearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResearchKeywordRepository extends JpaRepository<ResearchKeyword, UUID> {
    Optional<ResearchKeyword> findByKeywordIgnoreCaseAndDeletedAtIsNull(String keyword);
}
