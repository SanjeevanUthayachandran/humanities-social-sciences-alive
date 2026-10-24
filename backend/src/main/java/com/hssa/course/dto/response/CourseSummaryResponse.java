package com.hssa.course.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CourseSummaryResponse {
    private UUID id;
    private String title;
    private String slug;
    private String subtitle;
    private String description;
    private String categoryName;
    private String level;
    private String status;
    private UUID thumbnailMediaId;
    private String cardImageUrl;
    private Boolean isFree;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String currency;
    private BigDecimal durationHours;
    private List<String> tags;
}
