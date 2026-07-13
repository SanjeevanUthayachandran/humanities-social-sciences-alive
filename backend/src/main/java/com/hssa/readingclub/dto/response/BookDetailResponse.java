package com.hssa.readingclub.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class BookDetailResponse {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private UUID coverMediaId;
    private String description;
    private Integer totalPages;
    private UUID pdfMediaId;
    private String externalLink;
    private Boolean isFeatured;
    private Boolean isActive;
    private String categoryName;
}
