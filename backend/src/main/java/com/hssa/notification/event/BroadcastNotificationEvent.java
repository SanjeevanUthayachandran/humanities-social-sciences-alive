package com.hssa.notification.event;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
@Getter
public class BroadcastNotificationEvent extends ApplicationEvent {
    private final String title;
    public BroadcastNotificationEvent(Object source, String title) {
        super(source);
        this.title = title;
    }
}