package com.hssa.notification.event;
import java.util.UUID;
public class NotificationDeletedEvent extends NotificationApplicationEvent {
    public NotificationDeletedEvent(Object source, UUID notificationId) { super(source, notificationId); }
}