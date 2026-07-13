package com.hssa.certificate.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
@Builder
public class CertificateSummaryResponse {
    private UUID id;
    private String certificateNumber;
    private String studentName;
    private String courseName;
    private String status;
    private LocalDateTime issuedAt;
}
