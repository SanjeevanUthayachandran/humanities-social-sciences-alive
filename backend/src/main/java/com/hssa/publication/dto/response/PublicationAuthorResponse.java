package com.hssa.publication.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PublicationAuthorResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private boolean isPrimary;
}
