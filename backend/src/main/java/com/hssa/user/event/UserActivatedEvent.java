package com.hssa.user.event;
import java.util.UUID;
public class UserActivatedEvent extends UserEvent {
    public UserActivatedEvent(Object source, UUID userId) { super(source, userId); }
}
