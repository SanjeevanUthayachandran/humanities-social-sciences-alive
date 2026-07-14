package com.hssa.event.service;

import com.hssa.event.dto.request.EventAttendanceRequest;
import com.hssa.event.dto.response.EventRegistrationResponse;

import java.util.List;
import java.util.UUID;

public interface EventRegistrationService {
    EventRegistrationResponse register(UUID eventId, UUID userId);
    void cancelRegistration(UUID eventId, UUID userId);
    void approveRegistration(UUID eventId, UUID userId);
    void recordAttendance(UUID eventId, EventAttendanceRequest request, UUID recordedBy);
    List<EventRegistrationResponse> getRegistrations(UUID eventId);
}