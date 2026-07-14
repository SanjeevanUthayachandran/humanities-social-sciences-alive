package com.hssa.contact.event;
import java.util.UUID;
public class SupportTicketAssignedEvent extends ContactApplicationEvent {
    public SupportTicketAssignedEvent(Object source, UUID messageId) { super(source, messageId); }
}