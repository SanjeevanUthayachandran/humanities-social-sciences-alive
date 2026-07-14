package com.hssa.workshop.event;
import java.util.UUID;
public class WorkshopCancelledEvent extends WorkshopEvent {
    public WorkshopCancelledEvent(Object source, UUID workshopId) { super(source, workshopId); }
}
