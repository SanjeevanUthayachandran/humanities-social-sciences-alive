package com.hssa.media.event;
import java.util.UUID;
public class FileUploadedEvent extends MediaEvent {
    public FileUploadedEvent(Object source, UUID mediaId) { super(source, mediaId); }
}
