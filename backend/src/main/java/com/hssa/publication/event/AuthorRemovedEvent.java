package com.hssa.publication.event;
import java.util.UUID;
import lombok.Getter;
@Getter
public class AuthorRemovedEvent extends PublicationEvent {
    private final UUID userId;
    public AuthorRemovedEvent(Object source, UUID publicationId, UUID userId) { 
        super(source, publicationId); 
        this.userId = userId;
    }
}
