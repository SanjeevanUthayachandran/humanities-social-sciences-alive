package com.hssa.notification.event;
import java.util.UUID;
public class NotificationReadEvent extends NotificationApplicationEvent {
    public NotificationReadEvent(Object source, UUID notificationId) { super(source, notificationId); }
}