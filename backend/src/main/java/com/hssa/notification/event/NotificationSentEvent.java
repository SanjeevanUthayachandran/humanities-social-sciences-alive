package com.hssa.notification.event;
import java.util.UUID;
public class NotificationSentEvent extends NotificationApplicationEvent {
    public NotificationSentEvent(Object source, UUID notificationId) { super(source, notificationId); }
}