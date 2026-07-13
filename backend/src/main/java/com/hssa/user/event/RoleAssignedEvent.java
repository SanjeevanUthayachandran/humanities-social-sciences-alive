package com.hssa.user.event;
import java.util.UUID;
public class RoleAssignedEvent extends UserEvent {
    public RoleAssignedEvent(Object source, UUID userId) { super(source, userId); }
}
