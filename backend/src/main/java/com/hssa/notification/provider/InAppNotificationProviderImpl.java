package com.hssa.notification.provider;

import com.hssa.notification.entity.Notification;
import com.hssa.notification.entity.NotificationLog;
import com.hssa.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class InAppNotificationProviderImpl implements NotificationProvider {

    private final NotificationLogRepository logRepository;

    @Override
    public void send(Notification notification) {
        log.info("Sending IN_APP notification to user: {} (Title: {})", notification.getRecipient().getId(), notification.getTitle());
        try {
            // Notification entity itself acts as In-App storage
            saveLog(notification, "SUCCESS", null);
        } catch (Exception e) {
            log.error("Failed to process IN_APP notification for user: {}", notification.getRecipient().getId(), e);
            saveLog(notification, "FAILED", e.getMessage());
        }
    }

    private void saveLog(Notification notification, String status, String error) {
        NotificationLog nLog = NotificationLog.builder()
                .notification(notification)
                .deliveryChannel("IN_APP")
                .deliveryStatus(status)
                .sentAt(LocalDateTime.now())
                .errorMessage(error)
                .build();
        logRepository.save(nLog);
    }
}