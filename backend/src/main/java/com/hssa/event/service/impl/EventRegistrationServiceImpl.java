package com.hssa.event.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.event.dto.request.EventAttendanceRequest;
import com.hssa.event.dto.response.EventRegistrationResponse;
import com.hssa.event.entity.Event;
import com.hssa.event.entity.EventAttendance;
import com.hssa.event.entity.EventRegistration;
import com.hssa.event.entity.EventSession;
import com.hssa.event.event.EventAttendanceEvent;
import com.hssa.event.event.EventRegistrationEvent;
import com.hssa.event.repository.EventAttendanceRepository;
import com.hssa.event.repository.EventRegistrationRepository;
import com.hssa.event.repository.EventRepository;
import com.hssa.event.repository.EventSessionRepository;
import com.hssa.event.service.EventRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventRegistrationServiceImpl implements EventRegistrationService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final EventAttendanceRepository attendanceRepository;
    private final EventSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public EventRegistrationResponse register(UUID eventId, UUID userId) {
        Event event = eventRepository.findByIdAndDeletedAtIsNull(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (registrationRepository.existsByEventIdAndUserId(eventId, userId)) {
            throw new BusinessException("User already registered for this event");
        }
        if (event.getMaxParticipants() != null) {
            long registered = registrationRepository.countByEventIdAndStatus(eventId, "REGISTERED");
            if (registered >= event.getMaxParticipants()) {
                throw new BusinessException("Event is at full capacity");
            }
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        EventRegistration reg = EventRegistration.builder()
                .event(event)
                .user(user)
                .status("REGISTERED")
                .registeredAt(LocalDateTime.now())
                .build();
        reg = registrationRepository.save(reg);
        eventPublisher.publishEvent(new EventRegistrationEvent(this, eventId, userId));
        return toResponse(reg);
    }

    @Override
    @Transactional
    public void cancelRegistration(UUID eventId, UUID userId) {
        EventRegistration reg = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        reg.setStatus("CANCELLED");
        registrationRepository.save(reg);
    }

    @Override
    @Transactional
    public void approveRegistration(UUID eventId, UUID userId) {
        EventRegistration reg = registrationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        reg.setStatus("APPROVED");
        registrationRepository.save(reg);
    }

    @Override
    @Transactional
    public void recordAttendance(UUID eventId, EventAttendanceRequest request, UUID recordedBy) {
        EventRegistration reg = registrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        EventSession session = null;
        if (request.getSessionId() != null) {
            session = sessionRepository.findById(request.getSessionId()).orElse(null);
        }
        User recorder = userRepository.findById(recordedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        EventAttendance attendance = EventAttendance.builder()
                .registration(reg)
                .session(session)
                .joinedAt(request.getJoinedAt() != null ? request.getJoinedAt() : LocalDateTime.now())
                .leftAt(request.getLeftAt())
                .durationMinutes(request.getDurationMinutes())
                .createdBy(recorder)
                .build();
        attendanceRepository.save(attendance);
        eventPublisher.publishEvent(new EventAttendanceEvent(this, eventId, reg.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventRegistrationResponse> getRegistrations(UUID eventId) {
        return registrationRepository.findByEventId(eventId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventRegistrationResponse> getUserRegistrations(UUID userId) {
        return registrationRepository.findByUserIdOrderByRegisteredAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private EventRegistrationResponse toResponse(EventRegistration r) {
        return EventRegistrationResponse.builder()
                .id(r.getId())
                .eventId(r.getEvent().getId())
                .eventTitle(r.getEvent().getTitle())
                .userId(r.getUser().getId())
                .userFullName(r.getUser().getFirstName() + " " + r.getUser().getLastName())
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .startTime(r.getEvent().getStartTime())
                .build();
    }
}
