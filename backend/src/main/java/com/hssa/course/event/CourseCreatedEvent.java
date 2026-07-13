package com.hssa.course.event;
import java.util.UUID;
public class CourseCreatedEvent extends CourseEvent {
    public CourseCreatedEvent(Object source, UUID courseId) { super(source, courseId); }
}
