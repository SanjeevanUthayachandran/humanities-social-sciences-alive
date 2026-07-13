package com.hssa.publication.repository;

import com.hssa.publication.entity.PublicationAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublicationAuthorRepository extends JpaRepository<PublicationAuthor, UUID> {
    List<PublicationAuthor> findByPublicationId(UUID publicationId);
    Optional<PublicationAuthor> findByPublicationIdAndUserId(UUID publicationId, UUID userId);
    boolean existsByPublicationIdAndUserId(UUID publicationId, UUID userId);
}
