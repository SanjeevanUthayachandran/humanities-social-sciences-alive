package com.hssa.notification.provider;

import com.hssa.notification.entity.Notification;

public interface NotificationProvider {
    void send(Notification notification);
}