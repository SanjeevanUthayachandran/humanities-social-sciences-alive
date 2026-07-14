package com.hssa.workshop.event;
import java.util.UUID;
public class WorkshopCreatedEvent extends WorkshopEvent {
    public WorkshopCreatedEvent(Object source, UUID workshopId) { super(source, workshopId); }
}
