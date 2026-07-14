package com.hssa.workshop.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class WorkshopAttendanceEvent extends WorkshopEvent {
    private final UUID registrationId;
    public WorkshopAttendanceEvent(Object source, UUID workshopId, UUID registrationId) {
        super(source, workshopId);
        this.registrationId = registrationId;
    }
}
