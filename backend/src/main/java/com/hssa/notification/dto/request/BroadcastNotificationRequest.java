package com.hssa.notification.dto.request;

import com.hssa.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BroadcastNotificationRequest {
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    private String templateCode;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Message is required")
    private String message;
    private String actionUrl;
    private UUID imageMediaId;
}