package com.hssa.publication.dto.request;

import com.hssa.publication.enums.PublicationType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdatePublicationRequest {
    private String title;
    private UUID categoryId;
    private String abstractText;
    private String content;
    private PublicationType publicationType;
    private UUID coverMediaId;
    private List<String> keywords;
    private String doi;
}
