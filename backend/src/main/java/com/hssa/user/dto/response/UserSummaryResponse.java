package com.hssa.user.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserSummaryResponse {
    private UUID id;
    private String email;
    private String fullName;
    private boolean isActive;
    private String accountStatus;
    private LocalDateTime createdAt;
}
