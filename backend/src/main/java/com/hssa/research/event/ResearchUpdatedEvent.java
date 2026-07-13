package com.hssa.research.event;
import java.util.UUID;
public class ResearchUpdatedEvent extends ResearchEvent {
    public ResearchUpdatedEvent(Object source, UUID projectId) { super(source, projectId); }
}
