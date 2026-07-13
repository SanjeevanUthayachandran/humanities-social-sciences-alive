package com.hssa.course.event;
import java.util.UUID;
public class CourseArchivedEvent extends CourseEvent {
    public CourseArchivedEvent(Object source, UUID courseId) { super(source, courseId); }
}
