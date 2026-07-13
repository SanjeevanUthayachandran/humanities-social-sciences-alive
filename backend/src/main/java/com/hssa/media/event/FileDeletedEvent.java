package com.hssa.media.event;
import java.util.UUID;
public class FileDeletedEvent extends MediaEvent {
    public FileDeletedEvent(Object source, UUID mediaId) { super(source, mediaId); }
}
