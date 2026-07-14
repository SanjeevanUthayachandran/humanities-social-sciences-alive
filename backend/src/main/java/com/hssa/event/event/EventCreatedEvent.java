package com.hssa.event.event;
import java.util.UUID;
public class EventCreatedEvent extends EventApplicationEvent {
    public EventCreatedEvent(Object source, UUID eventId) { super(source, eventId); }
}