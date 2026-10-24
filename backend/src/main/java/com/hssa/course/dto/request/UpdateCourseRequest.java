package com.hssa.course.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class UpdateCourseRequest {
    private UUID categoryId;
    private String title;
    private String subtitle;
    private String description;
    private String language;
    private String level;
    private String visibility;
    
    private Boolean isFree;
    private BigDecimal price;
    private String currency;
    private BigDecimal discountedPrice;
    private BigDecimal durationHours;
    
    private Boolean certificationAvailable;
    
    private UUID thumbnailMediaId;
    private UUID bannerMediaId;
    private UUID promotionalVideoId;
    private String cardImageUrl;
    
    private List<String> tags;
    private List<UUID> instructorIds;
    
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
}
