package com.hssa.literary.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LiteraryWorkDetailResponse {
    private UUID id;
    private String title;
    private String slug;
    private String articleType;
    private String status;
    private String categoryName;
    private UUID authorId;
    private String authorFullName;
    private String summary;
    private String content;
    private String featuredImage;
    private String language;
    private Integer readingTime;
    private Boolean isFeatured;
    private Boolean allowComments;
    private Integer viewCount;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private List<String> tags;
    private long likeCount;
}
