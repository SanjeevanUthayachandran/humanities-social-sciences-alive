package com.hssa.lesson.event;
import java.util.UUID;
public class LessonPublishedEvent extends LessonEvent {
    public LessonPublishedEvent(Object source, UUID lessonId) { super(source, lessonId); }
}
