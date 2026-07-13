package com.hssa.literary.event;
import java.util.UUID;
public class LiteraryWorkUpdatedEvent extends LiteraryEvent {
    public LiteraryWorkUpdatedEvent(Object source, UUID articleId) { super(source, articleId); }
}
