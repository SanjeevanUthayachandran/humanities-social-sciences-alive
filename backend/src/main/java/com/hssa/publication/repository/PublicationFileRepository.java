package com.hssa.publication.repository;

import com.hssa.publication.entity.PublicationFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PublicationFileRepository extends JpaRepository<PublicationFile, UUID> {
    List<PublicationFile> findByPublicationIdAndDeletedAtIsNull(UUID publicationId);
}
