package com.hssa.event.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EventAttendanceRequest {
    private UUID registrationId;
    private UUID sessionId;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private Integer durationMinutes;
}