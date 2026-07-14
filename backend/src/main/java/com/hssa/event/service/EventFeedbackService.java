package com.hssa.event.service;

import com.hssa.event.dto.request.EventFeedbackRequest;

import java.util.UUID;

public interface EventFeedbackService {
    void submitFeedback(UUID eventId, EventFeedbackRequest request, UUID userId);
}