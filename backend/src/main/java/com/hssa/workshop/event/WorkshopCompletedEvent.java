package com.hssa.workshop.event;
import java.util.UUID;
public class WorkshopCompletedEvent extends WorkshopEvent {
    public WorkshopCompletedEvent(Object source, UUID workshopId) { super(source, workshopId); }
}
