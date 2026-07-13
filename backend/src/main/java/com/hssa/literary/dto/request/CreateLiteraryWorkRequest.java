package com.hssa.literary.dto.request;

import com.hssa.literary.enums.ArticleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateLiteraryWorkRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @NotNull(message = "Article type is required")
    private ArticleType articleType;

    @NotBlank(message = "Content is required")
    private String content;

    private String summary;
    private String featuredImage;
    private String language;
    private Integer readingTime;
    private Boolean allowComments;
    private List<String> tags;
}
