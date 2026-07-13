package com.hssa.media.repository;

import com.hssa.media.entity.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
import java.util.UUID;

public interface MediaFileRepository extends JpaRepository<MediaFile, UUID>, JpaSpecificationExecutor<MediaFile> {
    Optional<MediaFile> findByIdAndDeletedAtIsNull(UUID id);
}
