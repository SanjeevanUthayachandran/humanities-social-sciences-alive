package com.hssa.literary.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class LiteraryEvent extends ApplicationEvent {
    private final UUID articleId;
    public LiteraryEvent(Object source, UUID articleId) {
        super(source);
        this.articleId = articleId;
    }
}
