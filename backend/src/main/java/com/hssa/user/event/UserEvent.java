package com.hssa.user.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class UserEvent extends ApplicationEvent {
    private final UUID userId;
    
    public UserEvent(Object source, UUID userId) {
        super(source);
        this.userId = userId;
    }
}
