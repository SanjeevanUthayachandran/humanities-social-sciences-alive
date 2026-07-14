package com.hssa.notification.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.notification.dto.request.NotificationPreferenceRequest;
import com.hssa.notification.dto.response.NotificationPreferenceResponse;
import com.hssa.notification.entity.UserNotificationSetting;
import com.hssa.notification.repository.UserNotificationSettingRepository;
import com.hssa.notification.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {

    private final UserNotificationSettingRepository settingRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationPreferenceResponse getPreferences(UUID userId) {
        UserNotificationSetting settings = getOrCreateSettings(userId);
        return toResponse(settings);
    }

    @Override
    @Transactional
    public NotificationPreferenceResponse updatePreferences(UUID userId, NotificationPreferenceRequest request) {
        UserNotificationSetting settings = getOrCreateSettings(userId);

        if (request.getEmailEnabled() != null) settings.setEmailEnabled(request.getEmailEnabled());
        if (request.getPushEnabled() != null) settings.setPushEnabled(request.getPushEnabled());
        if (request.getSmsEnabled() != null) settings.setSmsEnabled(request.getSmsEnabled());
        if (request.getCourseNotifications() != null) settings.setCourseNotifications(request.getCourseNotifications());
        if (request.getPaymentNotifications() != null) settings.setPaymentNotifications(request.getPaymentNotifications());
        if (request.getCertificateNotifications() != null) settings.setCertificateNotifications(request.getCertificateNotifications());
        if (request.getWorkshopNotifications() != null) settings.setWorkshopNotifications(request.getWorkshopNotifications());
        if (request.getEventNotifications() != null) settings.setEventNotifications(request.getEventNotifications());
        if (request.getNewsNotifications() != null) settings.setNewsNotifications(request.getNewsNotifications());
        if (request.getAnnouncementNotifications() != null) settings.setAnnouncementNotifications(request.getAnnouncementNotifications());
        if (request.getSystemNotifications() != null) settings.setSystemNotifications(request.getSystemNotifications());

        settings = settingRepository.save(settings);
        return toResponse(settings);
    }

    private UserNotificationSetting getOrCreateSettings(UUID userId) {
        return settingRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    UserNotificationSetting setting = UserNotificationSetting.builder()
                            .user(user)
                            .build();
                    return settingRepository.save(setting);
                });
    }

    private NotificationPreferenceResponse toResponse(UserNotificationSetting s) {
        return NotificationPreferenceResponse.builder()
                .emailEnabled(s.getEmailEnabled())
                .pushEnabled(s.getPushEnabled())
                .smsEnabled(s.getSmsEnabled())
                .courseNotifications(s.getCourseNotifications())
                .paymentNotifications(s.getPaymentNotifications())
                .certificateNotifications(s.getCertificateNotifications())
                .workshopNotifications(s.getWorkshopNotifications())
                .eventNotifications(s.getEventNotifications())
                .newsNotifications(s.getNewsNotifications())
                .announcementNotifications(s.getAnnouncementNotifications())
                .systemNotifications(s.getSystemNotifications())
                .build();
    }
}