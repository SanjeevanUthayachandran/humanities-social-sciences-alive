package com.hssa.research.event;
import java.util.UUID;
import com.hssa.research.enums.ResearchStatus;
import lombok.Getter;
@Getter
public class ResearchStatusChangedEvent extends ResearchEvent {
    private final ResearchStatus newStatus;
    public ResearchStatusChangedEvent(Object source, UUID projectId, ResearchStatus newStatus) { 
        super(source, projectId); 
        this.newStatus = newStatus;
    }
}
