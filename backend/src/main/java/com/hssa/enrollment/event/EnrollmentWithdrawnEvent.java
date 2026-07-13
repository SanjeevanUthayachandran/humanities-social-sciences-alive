package com.hssa.enrollment.event;
import java.util.UUID;
public class EnrollmentWithdrawnEvent extends EnrollmentEvent {
    public EnrollmentWithdrawnEvent(Object source, UUID enrollmentId) { super(source, enrollmentId); }
}
