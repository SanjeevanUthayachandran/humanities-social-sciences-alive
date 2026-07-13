package com.hssa.media.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class MediaEvent extends ApplicationEvent {
    private final UUID mediaId;
    
    public MediaEvent(Object source, UUID mediaId) {
        super(source);
        this.mediaId = mediaId;
    }
}
