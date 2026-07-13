package com.hssa.research.event;
import java.util.UUID;
import lombok.Getter;
@Getter
public class ResearchMemberAddedEvent extends ResearchEvent {
    private final UUID userId;
    public ResearchMemberAddedEvent(Object source, UUID projectId, UUID userId) { 
        super(source, projectId); 
        this.userId = userId;
    }
}
