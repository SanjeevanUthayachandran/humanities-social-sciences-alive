package com.hssa.readingclub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class BookCategoryResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID parentId;
}
