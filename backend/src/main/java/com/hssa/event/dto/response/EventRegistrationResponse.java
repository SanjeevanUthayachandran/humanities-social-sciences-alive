package com.hssa.event.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class EventRegistrationResponse {
    private UUID id;
    private UUID eventId;
    private String eventTitle;
    private UUID userId;
    private String userFullName;
    private String status;
    private LocalDateTime registeredAt;
    private LocalDateTime startTime;
}
