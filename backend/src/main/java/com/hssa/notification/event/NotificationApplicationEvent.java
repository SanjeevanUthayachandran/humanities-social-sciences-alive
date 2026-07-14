package com.hssa.notification.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class NotificationApplicationEvent extends ApplicationEvent {
    private final UUID notificationId;
    public NotificationApplicationEvent(Object source, UUID notificationId) {
        super(source);
        this.notificationId = notificationId;
    }
}