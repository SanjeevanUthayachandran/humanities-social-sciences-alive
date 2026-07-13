package com.hssa.readingclub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReadingListResponse {
    private UUID id;
    private String title;
    private String description;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private List<BookSummaryResponse> books;
}
