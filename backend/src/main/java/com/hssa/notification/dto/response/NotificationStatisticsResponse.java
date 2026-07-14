package com.hssa.notification.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationStatisticsResponse {
    private long totalNotifications;
    private long unreadCount;
    private long readCount;
}