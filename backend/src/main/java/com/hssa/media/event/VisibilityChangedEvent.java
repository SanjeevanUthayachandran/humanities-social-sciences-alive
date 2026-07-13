package com.hssa.media.event;
import java.util.UUID;
public class VisibilityChangedEvent extends MediaEvent {
    public VisibilityChangedEvent(Object source, UUID mediaId) { super(source, mediaId); }
}
