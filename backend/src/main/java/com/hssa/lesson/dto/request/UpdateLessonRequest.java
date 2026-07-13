package com.hssa.lesson.dto.request;

import lombok.Data;

@Data
public class UpdateLessonRequest {
    private String title;
    private String description;
    private String content;
    private String lessonType;
    private Integer durationMinutes;
    private Boolean isFreePreview;
}
