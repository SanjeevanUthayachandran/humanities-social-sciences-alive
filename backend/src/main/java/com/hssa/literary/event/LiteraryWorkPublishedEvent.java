package com.hssa.literary.event;
import java.util.UUID;
public class LiteraryWorkPublishedEvent extends LiteraryEvent {
    public LiteraryWorkPublishedEvent(Object source, UUID articleId) { super(source, articleId); }
}
