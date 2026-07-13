package com.hssa.lesson.event;
import java.util.UUID;
public class LessonUpdatedEvent extends LessonEvent {
    public LessonUpdatedEvent(Object source, UUID lessonId) { super(source, lessonId); }
}
