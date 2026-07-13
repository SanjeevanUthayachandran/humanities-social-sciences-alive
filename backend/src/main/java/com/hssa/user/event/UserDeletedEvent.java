package com.hssa.user.event;
import java.util.UUID;
public class UserDeletedEvent extends UserEvent {
    public UserDeletedEvent(Object source, UUID userId) { super(source, userId); }
}
