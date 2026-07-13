package com.hssa.publication.repository;

import com.hssa.publication.entity.PublicationCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PublicationCategoryRepository extends JpaRepository<PublicationCategory, UUID> {
    Optional<PublicationCategory> findByIdAndDeletedAtIsNull(UUID id);
}
