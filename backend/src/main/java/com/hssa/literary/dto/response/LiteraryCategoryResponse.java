package com.hssa.literary.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LiteraryCategoryResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Boolean isActive;
    private UUID parentId;
}
