package com.hssa.literary.event;
import java.util.UUID;
public class LiteraryWorkArchivedEvent extends LiteraryEvent {
    public LiteraryWorkArchivedEvent(Object source, UUID articleId) { super(source, articleId); }
}
