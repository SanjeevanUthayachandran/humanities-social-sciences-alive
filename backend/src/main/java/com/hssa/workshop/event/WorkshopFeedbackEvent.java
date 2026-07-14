package com.hssa.workshop.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class WorkshopFeedbackEvent extends WorkshopEvent {
    private final UUID userId;
    public WorkshopFeedbackEvent(Object source, UUID workshopId, UUID userId) {
        super(source, workshopId);
        this.userId = userId;
    }
}
