package com.hssa.research.repository;

import com.hssa.research.entity.ResearchFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ResearchFileRepository extends JpaRepository<ResearchFile, UUID> {
    List<ResearchFile> findByProjectIdAndDeletedAtIsNull(UUID projectId);
}
