package com.hssa.event.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class EventFeedbackSubmittedEvent extends EventApplicationEvent {
    private final UUID userId;
    public EventFeedbackSubmittedEvent(Object source, UUID eventId, UUID userId) {
        super(source, eventId);
        this.userId = userId;
    }
}