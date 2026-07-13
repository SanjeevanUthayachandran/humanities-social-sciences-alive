package com.hssa.enrollment.event;
import java.util.UUID;
public class EnrollmentApprovedEvent extends EnrollmentEvent {
    public EnrollmentApprovedEvent(Object source, UUID enrollmentId) { super(source, enrollmentId); }
}
