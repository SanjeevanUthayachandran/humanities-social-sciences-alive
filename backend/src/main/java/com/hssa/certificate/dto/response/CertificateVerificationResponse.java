package com.hssa.certificate.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CertificateVerificationResponse {
    private String status;
    private String studentName;
    private String courseName;
    private LocalDateTime issueDate;
    private boolean isValid;
}
