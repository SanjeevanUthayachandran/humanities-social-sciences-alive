package com.hssa.publication.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddAuthorRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    private boolean isPrimary;
}
