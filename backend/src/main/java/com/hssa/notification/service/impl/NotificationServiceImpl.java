package com.hssa.notification.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.notification.dto.request.BroadcastNotificationRequest;
import com.hssa.notification.dto.request.CreateNotificationRequest;
import com.hssa.notification.dto.response.NotificationDetailResponse;
import com.hssa.notification.dto.response.NotificationStatisticsResponse;
import com.hssa.notification.dto.response.NotificationSummaryResponse;
import com.hssa.notification.entity.Notification;
import com.hssa.notification.entity.NotificationTemplate;
import com.hssa.notification.entity.UserNotificationSetting;
import com.hssa.notification.enums.NotificationStatus;
import com.hssa.notification.enums.NotificationType;
import com.hssa.notification.event.*;
import com.hssa.notification.provider.NotificationProvider;
import com.hssa.notification.repository.NotificationRepository;
import com.hssa.notification.repository.NotificationTemplateRepository;
import com.hssa.notification.repository.UserNotificationSettingRepository;
import com.hssa.notification.service.NotificationService;
import com.hssa.notification.specification.NotificationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationTemplateRepository templateRepository;
    private final UserNotificationSettingRepository settingRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final List<NotificationProvider> notificationProviders;

    @Override
    @Transactional
    public void sendNotification(CreateNotificationRequest request, UUID senderId) {
        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
        
        if (!shouldSend(recipient.getId(), request.getNotificationType())) {
            return;
        }

        User sender = null;
        if (senderId != null) {
            sender = userRepository.findById(senderId).orElse(null);
        }

        String title = request.getTitle();
        String message = request.getMessage();

        if (request.getTemplateCode() != null) {
            NotificationTemplate template = templateRepository.findByTemplateCode(request.getTemplateCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
            title = template.getTitleTemplate();
            message = template.getMessageTemplate();
        }

        Notification notification = Notification.builder()
                .sender(sender)
                .recipient(recipient)
                .notificationType(request.getNotificationType())
                .title(title)
                .message(message)
                .actionUrl(request.getActionUrl())
                .imageMediaId(request.getImageMediaId())
                .status(NotificationStatus.UNREAD)
                .build();
        
        notification = notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationCreatedEvent(this, notification.getId()));
        
        dispatchToProviders(notification);
    }

    @Override
    @Transactional
    public void broadcastNotification(BroadcastNotificationRequest request, UUID senderId) {
        User sender = userRepository.findById(senderId).orElse(null);
        String title = request.getTitle();
        String message = request.getMessage();

        if (request.getTemplateCode() != null) {
            NotificationTemplate template = templateRepository.findByTemplateCode(request.getTemplateCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Template not found"));
            title = template.getTitleTemplate();
            message = template.getMessageTemplate();
        }

        List<User> allUsers = userRepository.findAll();
        for (User recipient : allUsers) {
            if (shouldSend(recipient.getId(), request.getNotificationType())) {
                Notification notification = Notification.builder()
                        .sender(sender)
                        .recipient(recipient)
                        .notificationType(request.getNotificationType())
                        .title(title)
                        .message(message)
                        .actionUrl(request.getActionUrl())
                        .imageMediaId(request.getImageMediaId())
                        .status(NotificationStatus.UNREAD)
                        .build();
                notification = notificationRepository.save(notification);
                dispatchToProviders(notification);
            }
        }
        eventPublisher.publishEvent(new BroadcastNotificationEvent(this, title));
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId, UUID userId) {
        Notification notification = getActive(notificationId, userId);
        notification.setStatus(NotificationStatus.READ);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationReadEvent(this, notificationId));
    }

    @Override
    @Transactional
    public void markAllAsRead(UUID userId) {
        List<Notification> unreadList = notificationRepository.findAll(
                NotificationSpecification.filterBy(userId, null, "UNREAD", null)
        );
        LocalDateTime now = LocalDateTime.now();
        unreadList.forEach(n -> {
            n.setStatus(NotificationStatus.READ);
            n.setReadAt(now);
        });
        notificationRepository.saveAll(unreadList);
    }

    @Override
    @Transactional
    public void deleteNotification(UUID notificationId, UUID userId) {
        Notification notification = getActive(notificationId, userId);
        notification.setDeletedAt(LocalDateTime.now());
        notificationRepository.save(notification);
        eventPublisher.publishEvent(new NotificationDeletedEvent(this, notificationId));
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDetailResponse getNotification(UUID notificationId, UUID userId) {
        return buildDetail(getActive(notificationId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationSummaryResponse> listNotifications(UUID userId, Pageable pageable) {
        return notificationRepository.findAll(NotificationSpecification.filterBy(userId, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationSummaryResponse> searchNotifications(UUID userId, String type, String status, String keyword, Pageable pageable) {
        return notificationRepository.findAll(NotificationSpecification.filterBy(userId, type, status, keyword), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationStatisticsResponse getStatistics(UUID userId) {
        long total = notificationRepository.count(NotificationSpecification.filterBy(userId, null, null, null));
        long unread = notificationRepository.count(NotificationSpecification.filterBy(userId, null, "UNREAD", null));
        long read = notificationRepository.count(NotificationSpecification.filterBy(userId, null, "READ", null));
        return NotificationStatisticsResponse.builder()
                .totalNotifications(total)
                .unreadCount(unread)
                .readCount(read)
                .build();
    }

    private boolean shouldSend(UUID userId, NotificationType type) {
        UserNotificationSetting settings = settingRepository.findByUserId(userId).orElse(null);
        if (settings == null) return true;

        return switch (type) {
            case COURSE -> settings.getCourseNotifications();
            case PAYMENT -> settings.getPaymentNotifications();
            case CERTIFICATE -> settings.getCertificateNotifications();
            case EVENT -> settings.getEventNotifications();
            case WORKSHOP -> settings.getWorkshopNotifications();
            case NEWS -> settings.getNewsNotifications();
            case ANNOUNCEMENT -> settings.getAnnouncementNotifications();
            case SYSTEM -> settings.getSystemNotifications();
        };
    }

    private void dispatchToProviders(Notification notification) {
        UserNotificationSetting settings = settingRepository.findByUserId(notification.getRecipient().getId()).orElse(null);
        boolean emailEnabled = settings == null || settings.getEmailEnabled();
        
        for (NotificationProvider provider : notificationProviders) {
            if (provider.getClass().getSimpleName().startsWith("Email") && !emailEnabled) {
                continue;
            }
            provider.send(notification);
        }
    }

    private Notification getActive(UUID notificationId, UUID userId) {
        Notification notification = notificationRepository.findByIdAndDeletedAtIsNull(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));
        if (!notification.getRecipient().getId().equals(userId)) {
            throw new ResourceNotFoundException("Notification not found for this user");
        }
        return notification;
    }

    private NotificationDetailResponse buildDetail(Notification n) {
        return NotificationDetailResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .notificationType(n.getNotificationType().name())
                .status(n.getStatus().name())
                .actionUrl(n.getActionUrl())
                .imageMediaId(n.getImageMediaId())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }

    private NotificationSummaryResponse buildSummary(Notification n) {
        return NotificationSummaryResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .notificationType(n.getNotificationType().name())
                .status(n.getStatus().name())
                .createdAt(n.getCreatedAt())
                .readAt(n.getReadAt())
                .build();
    }
}