package com.hssa.lesson.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
import java.util.List;

@Data
@Builder
public class ModuleResponse {
    private UUID id;
    private UUID courseId;
    private String title;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
    // We can embed lessons directly or provide summary
    private List<LessonSummaryResponse> lessons;
}
