package com.hssa.user.event;
import java.util.UUID;
public class PermissionAssignedEvent extends UserEvent {
    public PermissionAssignedEvent(Object source, UUID userId) { super(source, userId); }
}
