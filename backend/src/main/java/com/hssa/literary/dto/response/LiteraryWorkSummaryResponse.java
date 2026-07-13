package com.hssa.literary.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LiteraryWorkSummaryResponse {
    private UUID id;
    private String title;
    private String slug;
    private String articleType;
    private String status;
    private String categoryName;
    private String authorFullName;
    private Boolean isFeatured;
    private Integer viewCount;
    private LocalDateTime publishedAt;
    private List<String> tags;
}
