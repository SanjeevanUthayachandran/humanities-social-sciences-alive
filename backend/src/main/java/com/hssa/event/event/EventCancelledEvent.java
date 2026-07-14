package com.hssa.event.event;
import java.util.UUID;
public class EventCancelledEvent extends EventApplicationEvent {
    public EventCancelledEvent(Object source, UUID eventId) { super(source, eventId); }
}