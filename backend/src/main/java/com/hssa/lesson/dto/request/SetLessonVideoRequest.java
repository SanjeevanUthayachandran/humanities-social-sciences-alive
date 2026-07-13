package com.hssa.lesson.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class SetLessonVideoRequest {
    @NotNull(message = "Media ID is required")
    private UUID mediaId;
    private Integer durationSeconds;
    private Boolean isDownloadable;
}
