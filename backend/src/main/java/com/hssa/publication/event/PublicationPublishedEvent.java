package com.hssa.publication.event;
import java.util.UUID;
public class PublicationPublishedEvent extends PublicationEvent {
    public PublicationPublishedEvent(Object source, UUID publicationId) { super(source, publicationId); }
}
