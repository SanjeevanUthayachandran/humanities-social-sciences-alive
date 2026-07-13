package com.hssa.certificate.dto.request;

import lombok.Data;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class ReissueCertificateRequest {
    @NotNull(message = "Certificate ID is required")
    private UUID certificateId;

    @NotBlank(message = "Reason is required")
    private String reason;
}
