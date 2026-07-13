package com.hssa.research.repository;

import com.hssa.research.entity.ResearchCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResearchCategoryRepository extends JpaRepository<ResearchCategory, UUID> {
    Optional<ResearchCategory> findByIdAndDeletedAtIsNull(UUID id);
}
