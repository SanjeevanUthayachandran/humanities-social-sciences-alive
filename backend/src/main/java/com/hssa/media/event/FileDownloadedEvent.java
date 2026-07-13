package com.hssa.media.event;
import java.util.UUID;
public class FileDownloadedEvent extends MediaEvent {
    public FileDownloadedEvent(Object source, UUID mediaId) { super(source, mediaId); }
}
