package com.hssa.course.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCourseRequest {
    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotBlank(message = "Title is required")
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
    
    private Boolean certificationAvailable;
    
    private UUID thumbnailMediaId;
    private UUID bannerMediaId;
    private UUID promotionalVideoId;
    
    private List<String> tags;
    private List<UUID> instructorIds;
    
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
}
