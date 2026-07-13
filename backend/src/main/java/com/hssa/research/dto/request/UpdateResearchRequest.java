package com.hssa.research.dto.request;

import com.hssa.research.enums.ResearchType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateResearchRequest {
    private String title;
    private UUID categoryId;
    private String abstractText;
    private String content;
    private ResearchType researchType;
    private UUID coverMediaId;
    private List<String> keywords;
}
