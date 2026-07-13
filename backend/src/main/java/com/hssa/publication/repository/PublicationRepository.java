package com.hssa.publication.repository;

import com.hssa.publication.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PublicationRepository extends JpaRepository<Publication, UUID>, JpaSpecificationExecutor<Publication> {
    Optional<Publication> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsByTitleAndDeletedAtIsNull(String title);
    boolean existsBySlugAndDeletedAtIsNull(String slug);
}
