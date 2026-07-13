package com.hssa.lesson.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class LessonEvent extends ApplicationEvent {
    private final UUID lessonId;
    
    public LessonEvent(Object source, UUID lessonId) {
        super(source);
        this.lessonId = lessonId;
    }
}
