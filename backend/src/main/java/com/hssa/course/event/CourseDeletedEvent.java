package com.hssa.course.event;
import java.util.UUID;
public class CourseDeletedEvent extends CourseEvent {
    public CourseDeletedEvent(Object source, UUID courseId) { super(source, courseId); }
}
