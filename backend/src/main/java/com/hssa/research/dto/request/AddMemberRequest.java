package com.hssa.research.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddMemberRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    private boolean isPrimary;
}
