package com.hssa.event.event;
import java.util.UUID;
public class EventPublishedEvent extends EventApplicationEvent {
    public EventPublishedEvent(Object source, UUID eventId) { super(source, eventId); }
}