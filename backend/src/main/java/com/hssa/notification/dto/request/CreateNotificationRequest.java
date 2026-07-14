package com.hssa.notification.dto.request;

import com.hssa.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateNotificationRequest {
    @NotNull(message = "Recipient ID is required")
    private UUID recipientId;
    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;
    private String templateCode;
    private String title;
    private String message;
    private String actionUrl;
    private UUID imageMediaId;
}