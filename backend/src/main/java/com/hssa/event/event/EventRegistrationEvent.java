package com.hssa.event.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class EventRegistrationEvent extends EventApplicationEvent {
    private final UUID userId;
    public EventRegistrationEvent(Object source, UUID eventId, UUID userId) {
        super(source, eventId);
        this.userId = userId;
    }
}