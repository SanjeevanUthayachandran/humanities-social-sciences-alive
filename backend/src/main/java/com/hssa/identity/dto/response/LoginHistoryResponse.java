package com.hssa.identity.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LoginHistoryResponse {
    private UUID id;
    private String ipAddress;
    private String userAgent;
    private String status;
    private String failureReason;
    private LocalDateTime loginAt;
}
