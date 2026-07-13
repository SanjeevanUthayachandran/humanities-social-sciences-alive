package com.hssa.research.event;
import java.util.UUID;
import lombok.Getter;
@Getter
public class ResearchMemberRemovedEvent extends ResearchEvent {
    private final UUID userId;
    public ResearchMemberRemovedEvent(Object source, UUID projectId, UUID userId) { 
        super(source, projectId); 
        this.userId = userId;
    }
}
