package com.hssa.notification.service;

import com.hssa.notification.dto.request.NotificationPreferenceRequest;
import com.hssa.notification.dto.response.NotificationPreferenceResponse;

import java.util.UUID;

public interface NotificationPreferenceService {
    NotificationPreferenceResponse getPreferences(UUID userId);
    NotificationPreferenceResponse updatePreferences(UUID userId, NotificationPreferenceRequest request);
}