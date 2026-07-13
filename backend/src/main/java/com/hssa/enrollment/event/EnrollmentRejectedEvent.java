package com.hssa.enrollment.event;
import java.util.UUID;
public class EnrollmentRejectedEvent extends EnrollmentEvent {
    public EnrollmentRejectedEvent(Object source, UUID enrollmentId) { super(source, enrollmentId); }
}
