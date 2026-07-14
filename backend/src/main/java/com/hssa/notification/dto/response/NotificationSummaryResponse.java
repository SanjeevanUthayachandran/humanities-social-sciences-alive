package com.hssa.notification.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationSummaryResponse {
    private UUID id;
    private String title;
    private String message;
    private String notificationType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
}