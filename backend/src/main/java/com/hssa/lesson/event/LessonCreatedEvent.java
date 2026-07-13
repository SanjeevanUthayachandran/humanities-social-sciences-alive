package com.hssa.lesson.event;
import java.util.UUID;
public class LessonCreatedEvent extends LessonEvent {
    public LessonCreatedEvent(Object source, UUID lessonId) { super(source, lessonId); }
}
