package com.hssa.readingclub.dto.request;

import lombok.Data;
import java.util.UUID;

@Data
public class UpdateBookRequest {
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private UUID categoryId;
    private UUID coverMediaId;
    private String description;
    private Integer totalPages;
    private UUID pdfMediaId;
    private String externalLink;
    private Boolean isFeatured;
    private Boolean isActive;
}
