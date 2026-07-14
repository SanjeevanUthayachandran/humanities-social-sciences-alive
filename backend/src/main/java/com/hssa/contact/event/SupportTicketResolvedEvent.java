package com.hssa.contact.event;
import java.util.UUID;
public class SupportTicketResolvedEvent extends ContactApplicationEvent {
    public SupportTicketResolvedEvent(Object source, UUID messageId) { super(source, messageId); }
}