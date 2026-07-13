package com.hssa.literary.event;
import lombok.Getter;
import java.util.UUID;
@Getter
public class CommentCreatedEvent extends LiteraryEvent {
    private final UUID commentId;
    public CommentCreatedEvent(Object source, UUID articleId, UUID commentId) {
        super(source, articleId);
        this.commentId = commentId;
    }
}
