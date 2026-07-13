package com.hssa.literary.dto.request;

import com.hssa.literary.enums.ArticleType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateLiteraryWorkRequest {
    private String title;
    private UUID categoryId;
    private ArticleType articleType;
    private String content;
    private String summary;
    private String featuredImage;
    private String language;
    private Integer readingTime;
    private Boolean allowComments;
    private Boolean isFeatured;
    private List<String> tags;
}
