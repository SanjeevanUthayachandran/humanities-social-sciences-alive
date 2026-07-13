package com.hssa.course.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CourseResponse {
    private UUID id;
    private UUID categoryId;
    private String title;
    private String slug;
    private String subtitle;
    private String description;
    private String language;
    private String level;
    private String status;
    private String visibility;
    private UUID thumbnailMediaId;
    private UUID bannerMediaId;
    private UUID promotionalVideoId;
    private Boolean isFree;
    private BigDecimal price;
    private String currency;
    private BigDecimal discountedPrice;
    private BigDecimal durationHours;
    private Boolean certificationAvailable;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tags;
    private List<UUID> instructorIds;
}
