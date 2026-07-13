package com.hssa.lesson.event;
import java.util.UUID;
public class LessonReorderedEvent extends LessonEvent {
    public LessonReorderedEvent(Object source, UUID lessonId) { super(source, lessonId); }
}
