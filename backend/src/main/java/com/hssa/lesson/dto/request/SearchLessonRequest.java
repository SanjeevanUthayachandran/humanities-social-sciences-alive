package com.hssa.lesson.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class SearchLessonRequest {
    private UUID courseId;
    private UUID moduleId;
    private String keyword;
    private String lessonType;
    private Boolean isActive;
    private Boolean isFreePreview;
}
