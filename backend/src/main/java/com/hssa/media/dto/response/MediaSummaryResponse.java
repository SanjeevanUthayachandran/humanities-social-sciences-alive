package com.hssa.media.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MediaSummaryResponse {
    private UUID id;
    private String originalName;
    private String mediaType;
    private String fileUrl;
    private String thumbnailUrl;
    private Long fileSize;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}
