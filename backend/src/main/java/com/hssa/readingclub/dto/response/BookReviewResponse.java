package com.hssa.readingclub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class BookReviewResponse {
    private UUID id;
    private UUID bookId;
    private UUID userId;
    private String userFullName;
    private Integer rating;
    private String review;
    private LocalDateTime createdAt;
}
