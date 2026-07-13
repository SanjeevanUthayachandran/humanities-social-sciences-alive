package com.hssa.lesson.dto.request;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class ReorderModuleRequest {
    private List<UUID> orderedModuleIds;
}
