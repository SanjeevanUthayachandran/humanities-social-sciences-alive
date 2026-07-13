package com.hssa.publication.repository;

import com.hssa.publication.entity.PublicationPublicationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PublicationPublicationKeywordRepository extends JpaRepository<PublicationPublicationKeyword, UUID> {
    List<PublicationPublicationKeyword> findByPublicationId(UUID publicationId);
    void deleteByPublicationIdAndKeywordId(UUID publicationId, UUID keywordId);
}
