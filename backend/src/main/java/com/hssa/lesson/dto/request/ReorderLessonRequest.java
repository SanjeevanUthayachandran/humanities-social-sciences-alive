package com.hssa.lesson.dto.request;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class ReorderLessonRequest {
    private List<UUID> orderedLessonIds;
}
