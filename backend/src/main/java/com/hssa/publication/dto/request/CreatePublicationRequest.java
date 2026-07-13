package com.hssa.publication.dto.request;

import com.hssa.publication.enums.PublicationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreatePublicationRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private String abstractText;
    private String content;

    @NotNull(message = "Publication Type is required")
    private PublicationType publicationType;

    private UUID coverMediaId;
    private List<String> keywords;
    private String doi;
}
