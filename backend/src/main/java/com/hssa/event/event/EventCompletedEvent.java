package com.hssa.event.event;
import java.util.UUID;
public class EventCompletedEvent extends EventApplicationEvent {
    public EventCompletedEvent(Object source, UUID eventId) { super(source, eventId); }
}