package com.hssa.media.repository;

import com.hssa.media.entity.MediaDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MediaDownloadRepository extends JpaRepository<MediaDownload, UUID> {
}
