package com.hssa.literary.event;
import java.util.UUID;
public class LiteraryWorkCreatedEvent extends LiteraryEvent {
    public LiteraryWorkCreatedEvent(Object source, UUID articleId) { super(source, articleId); }
}
