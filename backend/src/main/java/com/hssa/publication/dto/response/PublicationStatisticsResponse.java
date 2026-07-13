package com.hssa.publication.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublicationStatisticsResponse {
    private long totalPublications;
    private long totalDrafts;
    private long totalPublished;
    private long totalArchived;
}
