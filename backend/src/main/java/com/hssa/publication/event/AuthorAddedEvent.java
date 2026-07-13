package com.hssa.publication.event;
import java.util.UUID;
import lombok.Getter;
@Getter
public class AuthorAddedEvent extends PublicationEvent {
    private final UUID userId;
    public AuthorAddedEvent(Object source, UUID publicationId, UUID userId) { 
        super(source, publicationId); 
        this.userId = userId;
    }
}
