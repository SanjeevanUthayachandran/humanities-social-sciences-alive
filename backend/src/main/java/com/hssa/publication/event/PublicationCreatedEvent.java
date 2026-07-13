package com.hssa.publication.event;
import java.util.UUID;
public class PublicationCreatedEvent extends PublicationEvent {
    public PublicationCreatedEvent(Object source, UUID publicationId) { super(source, publicationId); }
}
