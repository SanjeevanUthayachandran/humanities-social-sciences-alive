package com.hssa.event.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.event.dto.request.EventFeedbackRequest;
import com.hssa.event.entity.Event;
import com.hssa.event.entity.EventFeedback;
import com.hssa.event.event.EventFeedbackSubmittedEvent;
import com.hssa.event.repository.EventFeedbackRepository;
import com.hssa.event.repository.EventRepository;
import com.hssa.event.service.EventFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventFeedbackServiceImpl implements EventFeedbackService {

    private final EventRepository eventRepository;
    private final EventFeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void submitFeedback(UUID eventId, EventFeedbackRequest request, UUID userId) {
        if (feedbackRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new BusinessException("Feedback already submitted for this event");
        }
        Event event = eventRepository.findByIdAndDeletedAtIsNull(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        EventFeedback feedback = EventFeedback.builder()
                .event(event)
                .user(user)
                .rating(request.getRating())
                .feedbackText(request.getFeedbackText())
                .isApproved(true)
                .build();
        feedbackRepository.save(feedback);
        eventPublisher.publishEvent(new EventFeedbackSubmittedEvent(this, eventId, userId));
    }
}