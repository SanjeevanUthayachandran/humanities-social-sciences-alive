package com.hssa.workshop.event;
import java.util.UUID;
public class WorkshopPublishedEvent extends WorkshopEvent {
    public WorkshopPublishedEvent(Object source, UUID workshopId) { super(source, workshopId); }
}
