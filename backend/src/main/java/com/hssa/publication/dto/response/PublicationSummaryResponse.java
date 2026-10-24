package com.hssa.publication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PublicationSummaryResponse {
    private UUID id;
    private String title;
    private String slug;
    private String abstractText;
    private String categoryName;
    private String status;
    private String publicationType;
    private LocalDateTime publishedDate;
}
