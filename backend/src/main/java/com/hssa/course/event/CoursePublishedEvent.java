package com.hssa.course.event;
import java.util.UUID;
public class CoursePublishedEvent extends CourseEvent {
    public CoursePublishedEvent(Object source, UUID courseId) { super(source, courseId); }
}
