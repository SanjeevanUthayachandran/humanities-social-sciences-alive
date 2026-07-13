package com.hssa.lesson.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LessonVideoResponse {
    private UUID id;
    private UUID mediaId;
    private Integer durationSeconds;
    private Boolean isDownloadable;
}
