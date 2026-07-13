package com.hssa.research.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ResearchDetailResponse {
    private UUID id;
    private String title;
    private String slug;
    private String abstractText;
    private String content;
    private String categoryName;
    private String status;
    private String researchType;
    private UUID coverMediaId;
    private LocalDateTime publishedDate;
    private List<String> keywords;
    private List<ResearchMemberResponse> members;
}
