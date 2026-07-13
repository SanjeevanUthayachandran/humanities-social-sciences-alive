package com.hssa.certificate.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
public class CertificateResponse {
    private UUID id;
    private UUID userId;
    private UUID courseId;
    private String certificateNumber;
    private String status;
    private LocalDateTime issuedAt;
    private String verificationCode;
}
