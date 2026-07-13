package com.hssa.lesson.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LessonSummaryResponse {
    private UUID id;
    private UUID moduleId;
    private String title;
    private String slug;
    private String lessonType;
    private Integer durationMinutes;
    private Boolean isFreePreview;
    private Integer displayOrder;
    private Boolean isActive;
}
