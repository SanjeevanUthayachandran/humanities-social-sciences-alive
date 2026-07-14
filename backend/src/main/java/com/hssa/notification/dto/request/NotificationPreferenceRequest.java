package com.hssa.notification.dto.request;

import lombok.Data;

@Data
public class NotificationPreferenceRequest {
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