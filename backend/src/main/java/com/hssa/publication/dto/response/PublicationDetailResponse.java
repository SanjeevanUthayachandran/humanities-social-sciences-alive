package com.hssa.publication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class PublicationDetailResponse {
    private UUID id;
    private String title;
    private String slug;
    private String abstractText;
    private String content;
    private String categoryName;
    private String status;
    private String publicationType;
    private UUID coverMediaId;
    private LocalDateTime publishedDate;
    private String doi;
    private List<String> keywords;
    private List<PublicationAuthorResponse> authors;
}
