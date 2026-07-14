package com.hssa.workshop.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class WorkshopEvent extends ApplicationEvent {
    private final UUID workshopId;
    public WorkshopEvent(Object source, UUID workshopId) {
        super(source);
        this.workshopId = workshopId;
    }
}
