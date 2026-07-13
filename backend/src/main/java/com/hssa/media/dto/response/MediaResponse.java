package com.hssa.media.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MediaResponse {
    private UUID id;
    private UUID folderId;
    private UUID uploadedBy;
    private String mediaType;
    private String storageProvider;
    private String originalName;
    private String fileExtension;
    private String mimeType;
    private Long fileSize;
    private String fileUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private Integer durationSeconds;
    private String checksum;
    private String altText;
    private String caption;
    private Boolean isPublic;
    private Integer downloadCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
