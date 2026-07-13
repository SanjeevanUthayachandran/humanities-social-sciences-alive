package com.hssa.literary.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class LikeAddedEvent extends LiteraryEvent {
    private final UUID userId;
    public LikeAddedEvent(Object source, UUID articleId, UUID userId) {
        super(source, articleId);
        this.userId = userId;
    }
}
