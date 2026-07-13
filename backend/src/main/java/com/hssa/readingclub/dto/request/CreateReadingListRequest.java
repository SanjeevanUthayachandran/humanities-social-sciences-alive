package com.hssa.readingclub.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateReadingListRequest {
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    private Boolean isPublic;
}
