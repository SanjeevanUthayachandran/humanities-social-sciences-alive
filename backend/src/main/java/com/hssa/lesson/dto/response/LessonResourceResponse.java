package com.hssa.lesson.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LessonResourceResponse {
    private UUID id;
    private String title;
    private String resourceType;
    private UUID mediaId;
    private String externalLink;
    private Boolean isDownloadable;
    private Integer displayOrder;
}
