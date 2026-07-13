package com.hssa.lesson.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateLessonRequest {
    @NotNull(message = "Module ID is required")
    private UUID moduleId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String content;

    @NotBlank(message = "Lesson Type is required")
    private String lessonType;

    private Integer durationMinutes;
    private Boolean isFreePreview;
}
