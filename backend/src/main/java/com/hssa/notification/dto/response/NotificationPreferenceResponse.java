package com.hssa.notification.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationPreferenceResponse {
    private Boolean emailEnabled;
    private Boolean pushEnabled;
    private Boolean smsEnabled;
    private Boolean courseNotifications;
    private Boolean paymentNotifications;
    private Boolean certificateNotifications;
    private Boolean workshopNotifications;
    private Boolean eventNotifications;
    private Boolean newsNotifications;
    private Boolean announcementNotifications;
    private Boolean systemNotifications;
}