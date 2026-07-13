package com.hssa.course.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class CourseEvent extends ApplicationEvent {
    private final UUID courseId;
    
    public CourseEvent(Object source, UUID courseId) {
        super(source);
        this.courseId = courseId;
    }
}
