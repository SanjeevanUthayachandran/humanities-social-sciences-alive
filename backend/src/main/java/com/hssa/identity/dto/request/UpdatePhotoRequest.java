package com.hssa.identity.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class UpdatePhotoRequest {
    @NotNull(message = "Media ID is required")
    private UUID mediaId;
}
