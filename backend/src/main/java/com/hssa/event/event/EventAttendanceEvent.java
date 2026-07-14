package com.hssa.event.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class EventAttendanceEvent extends EventApplicationEvent {
    private final UUID registrationId;
    public EventAttendanceEvent(Object source, UUID eventId, UUID registrationId) {
        super(source, eventId);
        this.registrationId = registrationId;
    }
}