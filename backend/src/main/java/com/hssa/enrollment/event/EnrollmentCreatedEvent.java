package com.hssa.enrollment.event;
import java.util.UUID;
public class EnrollmentCreatedEvent extends EnrollmentEvent {
    public EnrollmentCreatedEvent(Object source, UUID enrollmentId) { super(source, enrollmentId); }
}
