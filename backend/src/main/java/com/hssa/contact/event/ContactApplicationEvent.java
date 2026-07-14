package com.hssa.contact.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class ContactApplicationEvent extends ApplicationEvent {
    private final UUID messageId;
    public ContactApplicationEvent(Object source, UUID messageId) {
        super(source);
        this.messageId = messageId;
    }
}