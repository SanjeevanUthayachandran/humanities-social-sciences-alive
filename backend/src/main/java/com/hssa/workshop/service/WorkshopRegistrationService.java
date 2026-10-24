package com.hssa.workshop.service;

import com.hssa.workshop.dto.request.AttendanceRequest;
import com.hssa.workshop.dto.response.WorkshopRegistrationResponse;

import java.util.List;
import java.util.UUID;

public interface WorkshopRegistrationService {
    WorkshopRegistrationResponse register(UUID workshopId, UUID userId);
    void cancelRegistration(UUID workshopId, UUID userId);
    void approveRegistration(UUID workshopId, UUID userId);
    void recordAttendance(UUID workshopId, AttendanceRequest request, UUID recordedBy);
    List<WorkshopRegistrationResponse> getRegistrations(UUID workshopId);
    List<WorkshopRegistrationResponse> getUserRegistrations(UUID userId);
}
