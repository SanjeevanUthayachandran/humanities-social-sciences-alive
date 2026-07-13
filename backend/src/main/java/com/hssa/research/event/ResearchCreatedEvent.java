package com.hssa.research.event;
import java.util.UUID;
public class ResearchCreatedEvent extends ResearchEvent {
    public ResearchCreatedEvent(Object source, UUID projectId) { super(source, projectId); }
}
