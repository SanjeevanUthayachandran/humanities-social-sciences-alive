package com.hssa.workshop.repository;

import com.hssa.workshop.entity.Workshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface WorkshopRepository extends JpaRepository<Workshop, UUID>, JpaSpecificationExecutor<Workshop> {
    Optional<Workshop> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsBySlugAndDeletedAtIsNull(String slug);
}
