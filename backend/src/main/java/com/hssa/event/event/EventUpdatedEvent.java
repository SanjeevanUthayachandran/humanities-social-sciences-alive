package com.hssa.event.event;
import java.util.UUID;
public class EventUpdatedEvent extends EventApplicationEvent {
    public EventUpdatedEvent(Object source, UUID eventId) { super(source, eventId); }
}