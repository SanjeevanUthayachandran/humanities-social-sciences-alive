package com.hssa.workshop.dto.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AttendanceRequest {
    private UUID registrationId;
    private UUID sessionId;
    private LocalDateTime joinedAt;
    private LocalDateTime leftAt;
    private Integer durationMinutes;
}
