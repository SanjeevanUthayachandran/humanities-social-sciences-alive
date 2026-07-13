package com.hssa.publication.event;
import java.util.UUID;
public class PublicationArchivedEvent extends PublicationEvent {
    public PublicationArchivedEvent(Object source, UUID publicationId) { super(source, publicationId); }
}
