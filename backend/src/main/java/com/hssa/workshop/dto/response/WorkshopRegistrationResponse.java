package com.hssa.workshop.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class WorkshopRegistrationResponse {
    private UUID id;
    private UUID workshopId;
    private String workshopTitle;
    private UUID userId;
    private String userFullName;
    private String status;
    private LocalDateTime registeredAt;
    private LocalDateTime startTime;
}
