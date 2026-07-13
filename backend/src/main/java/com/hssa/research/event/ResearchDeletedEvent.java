package com.hssa.research.event;
import java.util.UUID;
public class ResearchDeletedEvent extends ResearchEvent {
    public ResearchDeletedEvent(Object source, UUID projectId) { super(source, projectId); }
}
