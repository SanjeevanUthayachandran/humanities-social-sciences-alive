package com.hssa.course.event;
import java.util.UUID;
public class CourseUpdatedEvent extends CourseEvent {
    public CourseUpdatedEvent(Object source, UUID courseId) { super(source, courseId); }
}
