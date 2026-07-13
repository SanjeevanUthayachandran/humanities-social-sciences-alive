package com.hssa.publication.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class PublicationEvent extends ApplicationEvent {
    private final UUID publicationId;
    public PublicationEvent(Object source, UUID publicationId) {
        super(source);
        this.publicationId = publicationId;
    }
}
