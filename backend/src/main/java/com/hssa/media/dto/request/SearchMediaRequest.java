package com.hssa.media.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SearchMediaRequest {
    private String keyword;
    private String mediaType;
    private String extension;
    private Boolean isPublic;
    private UUID ownerId;
    private UUID folderId;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private Long minSize;
    private Long maxSize;
}
