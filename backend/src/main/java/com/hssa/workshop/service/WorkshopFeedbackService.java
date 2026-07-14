package com.hssa.workshop.service;

import com.hssa.workshop.dto.request.WorkshopFeedbackRequest;

import java.util.UUID;

public interface WorkshopFeedbackService {
    void submitFeedback(UUID workshopId, WorkshopFeedbackRequest request, UUID userId);
}
