package com.hssa.enrollment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class EnrollmentEvent extends ApplicationEvent {
    private final UUID enrollmentId;
    
    public EnrollmentEvent(Object source, UUID enrollmentId) {
        super(source);
        this.enrollmentId = enrollmentId;
    }
}
