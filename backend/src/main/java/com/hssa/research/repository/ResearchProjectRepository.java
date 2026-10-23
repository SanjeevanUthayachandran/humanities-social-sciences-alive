package com.hssa.research.repository;

import com.hssa.research.entity.ResearchProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ResearchProjectRepository extends JpaRepository<ResearchProject, UUID>, JpaSpecificationExecutor<ResearchProject> {
    Optional<ResearchProject> findByIdAndDeletedAtIsNull(UUID id);
    Optional<ResearchProject> findBySlugAndDeletedAtIsNull(String slug);
    boolean existsByTitleAndDeletedAtIsNull(String title);
    boolean existsBySlugAndDeletedAtIsNull(String slug);
}
