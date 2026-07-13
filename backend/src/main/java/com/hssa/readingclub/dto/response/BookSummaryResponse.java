package com.hssa.readingclub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class BookSummaryResponse {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private String categoryName;
    private Boolean isFeatured;
    private UUID coverMediaId;
}
