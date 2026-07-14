package com.hssa.workshop.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.workshop.dto.request.WorkshopFeedbackRequest;
import com.hssa.workshop.entity.Workshop;
import com.hssa.workshop.entity.WorkshopFeedback;
import com.hssa.workshop.event.WorkshopFeedbackEvent;
import com.hssa.workshop.repository.WorkshopFeedbackRepository;
import com.hssa.workshop.repository.WorkshopRepository;
import com.hssa.workshop.service.WorkshopFeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkshopFeedbackServiceImpl implements WorkshopFeedbackService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopFeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void submitFeedback(UUID workshopId, WorkshopFeedbackRequest request, UUID userId) {
        if (feedbackRepository.existsByWorkshopIdAndUserId(workshopId, userId)) {
            throw new BusinessException("Feedback already submitted for this workshop");
        }
        Workshop workshop = workshopRepository.findByIdAndDeletedAtIsNull(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        WorkshopFeedback feedback = WorkshopFeedback.builder()
                .workshop(workshop)
                .user(user)
                .rating(request.getRating())
                .feedbackText(request.getFeedbackText())
                .isApproved(true)
                .build();
        feedbackRepository.save(feedback);
        eventPublisher.publishEvent(new WorkshopFeedbackEvent(this, workshopId, userId));
    }
}
