package com.hssa.research.event;
import java.util.UUID;
public class ResearchArchivedEvent extends ResearchEvent {
    public ResearchArchivedEvent(Object source, UUID projectId) { super(source, projectId); }
}
