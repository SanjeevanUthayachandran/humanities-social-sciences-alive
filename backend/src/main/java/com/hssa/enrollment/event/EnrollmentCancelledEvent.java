package com.hssa.enrollment.event;
import java.util.UUID;
public class EnrollmentCancelledEvent extends EnrollmentEvent {
    public EnrollmentCancelledEvent(Object source, UUID enrollmentId) { super(source, enrollmentId); }
}
