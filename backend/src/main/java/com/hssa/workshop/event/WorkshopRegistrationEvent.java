package com.hssa.workshop.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class WorkshopRegistrationEvent extends WorkshopEvent {
    private final UUID userId;
    public WorkshopRegistrationEvent(Object source, UUID workshopId, UUID userId) {
        super(source, workshopId);
        this.userId = userId;
    }
}
