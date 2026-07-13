package com.hssa.research.dto.request;

import com.hssa.research.enums.ResearchType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateResearchRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private String abstractText;
    private String content;

    @NotNull(message = "Research Type is required")
    private ResearchType researchType;

    private UUID coverMediaId;
    private List<String> keywords;
}
