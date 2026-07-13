package com.hssa.publication.event;
import java.util.UUID;
public class PublicationDeletedEvent extends PublicationEvent {
    public PublicationDeletedEvent(Object source, UUID publicationId) { super(source, publicationId); }
}
