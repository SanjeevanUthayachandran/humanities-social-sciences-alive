package com.hssa.research.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ResearchSummaryResponse {
    private UUID id;
    private String title;
    private String categoryName;
    private String status;
    private String researchType;
    private LocalDateTime publishedDate;
}
