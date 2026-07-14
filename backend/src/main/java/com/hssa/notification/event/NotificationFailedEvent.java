package com.hssa.notification.event;
import java.util.UUID;
public class NotificationFailedEvent extends NotificationApplicationEvent {
    public NotificationFailedEvent(Object source, UUID notificationId) { super(source, notificationId); }
}