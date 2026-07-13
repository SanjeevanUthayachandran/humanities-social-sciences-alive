package com.hssa.lesson.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateLessonResourceRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Resource Type is required")
    private String resourceType;
    private UUID mediaId;
    private String externalLink;
    private Boolean isDownloadable;
}
