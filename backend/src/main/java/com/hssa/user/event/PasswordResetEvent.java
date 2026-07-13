package com.hssa.user.event;
import java.util.UUID;
public class PasswordResetEvent extends UserEvent {
    public PasswordResetEvent(Object source, UUID userId) { super(source, userId); }
}
