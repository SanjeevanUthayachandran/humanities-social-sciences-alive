package com.hssa.workshop.event;
import java.util.UUID;
public class WorkshopUpdatedEvent extends WorkshopEvent {
    public WorkshopUpdatedEvent(Object source, UUID workshopId) { super(source, workshopId); }
}
