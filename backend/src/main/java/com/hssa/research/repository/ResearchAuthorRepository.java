package com.hssa.research.repository;

import com.hssa.research.entity.ResearchAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResearchAuthorRepository extends JpaRepository<ResearchAuthor, UUID> {
    List<ResearchAuthor> findByProjectIdAndDeletedAtIsNull(UUID projectId);
    Optional<ResearchAuthor> findByProjectIdAndUserIdAndDeletedAtIsNull(UUID projectId, UUID userId);
    boolean existsByProjectIdAndUserIdAndDeletedAtIsNull(UUID projectId, UUID userId);
}
