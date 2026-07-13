package com.hssa.publication.event;
import java.util.UUID;
public class PublicationUpdatedEvent extends PublicationEvent {
    public PublicationUpdatedEvent(Object source, UUID publicationId) { super(source, publicationId); }
}
