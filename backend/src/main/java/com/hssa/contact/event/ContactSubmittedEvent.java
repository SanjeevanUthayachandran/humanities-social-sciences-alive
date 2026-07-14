package com.hssa.contact.event;
import java.util.UUID;
public class ContactSubmittedEvent extends ContactApplicationEvent {
    public ContactSubmittedEvent(Object source, UUID messageId) { super(source, messageId); }
}