package com.hssa.event.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class EventApplicationEvent extends ApplicationEvent {
    private final UUID eventId;
    public EventApplicationEvent(Object source, UUID eventId) {
        super(source);
        this.eventId = eventId;
    }
}