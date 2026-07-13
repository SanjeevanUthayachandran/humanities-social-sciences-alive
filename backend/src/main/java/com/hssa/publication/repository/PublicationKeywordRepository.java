package com.hssa.publication.repository;

import com.hssa.publication.entity.PublicationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PublicationKeywordRepository extends JpaRepository<PublicationKeyword, UUID> {
    Optional<PublicationKeyword> findByKeywordIgnoreCaseAndDeletedAtIsNull(String keyword);
}
