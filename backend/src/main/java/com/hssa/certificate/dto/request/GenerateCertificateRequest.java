package com.hssa.certificate.dto.request;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;

@Data
public class GenerateCertificateRequest {
    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Course ID is required")
    private UUID courseId;

    private UUID templateId;
}
