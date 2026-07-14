package com.hssa.notification.event;
import java.util.UUID;
public class NotificationCreatedEvent extends NotificationApplicationEvent {
    public NotificationCreatedEvent(Object source, UUID notificationId) { super(source, notificationId); }
}