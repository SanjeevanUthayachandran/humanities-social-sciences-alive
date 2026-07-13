package com.hssa.media.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class FileMetadataResponse {
    private UUID id;
    private String originalName;
    private String storedName;
    private String extension;
    private String mimeType;
    private Long size;
    private String checksum;
    private UUID ownerId;
    private LocalDateTime uploadTime;
    private Boolean isPublic;
    private Integer downloadCount;
}
