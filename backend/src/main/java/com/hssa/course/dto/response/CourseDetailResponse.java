package com.hssa.course.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CourseDetailResponse {
    private UUID id;
    private String title;
    private String slug;
    private String subtitle;
    private String description;
    private String language;
    private String level;
    private String categoryName;
    private UUID thumbnailMediaId;
    private UUID bannerMediaId;
    private UUID promotionalVideoId;
    private String cardImageUrl;
    private Boolean isFree;
    private BigDecimal price;
    private BigDecimal discountedPrice;
    private String currency;
    private BigDecimal durationHours;
    private Boolean certificationAvailable;
    private List<String> tags;
    // We would map full instructor details here ideally
    private List<UUID> instructorIds; 
    private LocalDateTime publishedAt;
}
