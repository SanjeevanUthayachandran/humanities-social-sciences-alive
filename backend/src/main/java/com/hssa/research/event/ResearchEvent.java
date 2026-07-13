package com.hssa.research.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class ResearchEvent extends ApplicationEvent {
    private final UUID projectId;
    public ResearchEvent(Object source, UUID projectId) {
        super(source);
        this.projectId = projectId;
    }
}
