package com.hssa.user.event;
import java.util.UUID;
public class UserDeactivatedEvent extends UserEvent {
    public UserDeactivatedEvent(Object source, UUID userId) { super(source, userId); }
}
