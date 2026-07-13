package com.hssa.lesson.event;
import java.util.UUID;
public class LessonDeletedEvent extends LessonEvent {
    public LessonDeletedEvent(Object source, UUID lessonId) { super(source, lessonId); }
}
