package com.hssa.notification.service;

import com.hssa.notification.dto.request.BroadcastNotificationRequest;
import com.hssa.notification.dto.request.CreateNotificationRequest;
import com.hssa.notification.dto.response.NotificationDetailResponse;
import com.hssa.notification.dto.response.NotificationStatisticsResponse;
import com.hssa.notification.dto.response.NotificationSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface NotificationService {
    void sendNotification(CreateNotificationRequest request, UUID senderId);
    void broadcastNotification(BroadcastNotificationRequest request, UUID senderId);
    void markAsRead(UUID notificationId, UUID userId);
    void markAllAsRead(UUID userId);
    void deleteNotification(UUID notificationId, UUID userId);
    NotificationDetailResponse getNotification(UUID notificationId, UUID userId);
    Page<NotificationSummaryResponse> listNotifications(UUID userId, Pageable pageable);
    Page<NotificationSummaryResponse> searchNotifications(UUID userId, String type, String status, String keyword, Pageable pageable);
    NotificationStatisticsResponse getStatistics(UUID userId);
}