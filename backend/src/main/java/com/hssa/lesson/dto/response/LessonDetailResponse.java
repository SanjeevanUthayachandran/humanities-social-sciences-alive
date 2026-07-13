package com.hssa.lesson.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LessonDetailResponse {
    private UUID id;
    private UUID moduleId;
    private String moduleTitle;
    private UUID courseId;
    private String title;
    private String slug;
    private String description;
    private String content;
    private String lessonType;
    private Integer durationMinutes;
    private Boolean isFreePreview;
    private Integer displayOrder;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private LessonVideoResponse video;
    private List<LessonResourceResponse> resources;
}
