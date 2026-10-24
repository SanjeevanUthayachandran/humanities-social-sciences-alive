package com.hssa.event.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.event.dto.request.CreateEventRequest;
import com.hssa.event.dto.request.UpdateEventRequest;
import com.hssa.event.dto.response.EventDetailResponse;
import com.hssa.event.dto.response.EventStatisticsResponse;
import com.hssa.event.dto.response.EventSummaryResponse;
import com.hssa.event.entity.Event;
import com.hssa.event.enums.EventStatus;
import com.hssa.event.event.*;
import com.hssa.event.repository.EventRegistrationRepository;
import com.hssa.event.repository.EventRepository;
import com.hssa.event.service.EventService;
import com.hssa.event.specification.EventSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public EventDetailResponse createEvent(CreateEventRequest request, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String slug = request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + System.currentTimeMillis();
        Event event = Event.builder()
                .title(request.getTitle())
                .slug(slug)
                .description(request.getDescription())
                .eventType(request.getEventType())
                .coverMediaId(request.getCoverMediaId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .locationType(request.getLocationType() != null ? request.getLocationType() : "ONLINE")
                .locationDetails(request.getLocationDetails())
                .registrationDeadline(request.getRegistrationDeadline())
                .timezone(request.getTimezone())
                .meetingLink(request.getMeetingLink())
                .meetingPassword(request.getMeetingPassword())
                .maxParticipants(request.getMaxParticipants())
                .isFree(request.getIsFree() != null ? request.getIsFree() : true)
                .price(request.getPrice())
                .createdBy(creator)
                .status(EventStatus.UPCOMING)
                .build();
        event = eventRepository.save(event);
        eventPublisher.publishEvent(new EventCreatedEvent(this, event.getId()));
        return buildDetail(event);
    }

    @Override
    @Transactional
    public EventDetailResponse updateEvent(UUID id, UpdateEventRequest request, UUID updaterId) {
        Event event = eventRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        User updater = userRepository.findById(updaterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getTitle() != null) event.setTitle(request.getTitle());
        if (request.getDescription() != null) event.setDescription(request.getDescription());
        if (request.getEventType() != null) event.setEventType(request.getEventType());
        if (request.getCoverMediaId() != null) event.setCoverMediaId(request.getCoverMediaId());
        if (request.getStartTime() != null) event.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) event.setEndTime(request.getEndTime());
        if (request.getLocationType() != null) event.setLocationType(request.getLocationType());
        if (request.getLocationDetails() != null) event.setLocationDetails(request.getLocationDetails());
        if (request.getRegistrationDeadline() != null) event.setRegistrationDeadline(request.getRegistrationDeadline());
        if (request.getTimezone() != null) event.setTimezone(request.getTimezone());
        if (request.getMeetingLink() != null) event.setMeetingLink(request.getMeetingLink());
        if (request.getMeetingPassword() != null) event.setMeetingPassword(request.getMeetingPassword());
        if (request.getMaxParticipants() != null) event.setMaxParticipants(request.getMaxParticipants());
        if (request.getIsFree() != null) event.setIsFree(request.getIsFree());
        if (request.getPrice() != null) event.setPrice(request.getPrice());
        event.setUpdatedBy(updater);
        event = eventRepository.save(event);
        eventPublisher.publishEvent(new EventUpdatedEvent(this, event.getId()));
        return buildDetail(event);
    }

    @Override
    @Transactional
    public void deleteEvent(UUID id) {
        Event e = eventRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        e.setDeletedAt(LocalDateTime.now());
        eventRepository.save(e);
    }

    @Override
    @Transactional
    public void restoreEvent(UUID id) {
        Event e = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        e.setDeletedAt(null);
        eventRepository.save(e);
    }

    @Override
    @Transactional
    public void publishEvent(UUID id) {
        Event e = getActive(id);
        e.setStatus(EventStatus.OPEN);
        eventRepository.save(e);
        eventPublisher.publishEvent(new EventPublishedEvent(this, id));
    }

    @Override
    @Transactional
    public void cancelEvent(UUID id) {
        Event e = getActive(id);
        e.setStatus(EventStatus.CANCELLED);
        eventRepository.save(e);
        eventPublisher.publishEvent(new EventCancelledEvent(this, id));
    }

    @Override
    @Transactional
    public void completeEvent(UUID id) {
        Event e = getActive(id);
        e.setStatus(EventStatus.COMPLETED);
        eventRepository.save(e);
        eventPublisher.publishEvent(new EventCompletedEvent(this, id));
    }

    @Override
    @Transactional(readOnly = true)
    public EventDetailResponse getEvent(UUID id) {
        return buildDetail(getActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> listEvents(Pageable pageable) {
        return eventRepository.findAll(EventSpecification.filterBy(null, null, null, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> searchEvents(String title, String status, String eventType, String locationType, Boolean isFree, Pageable pageable) {
        return eventRepository.findAll(EventSpecification.filterBy(title, status, eventType, locationType, isFree, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> getUpcoming(Pageable pageable) {
        return eventRepository.findAll(EventSpecification.filterBy(null, "UPCOMING", null, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventSummaryResponse> getFeatured(Pageable pageable) {
        return eventRepository.findAll(EventSpecification.filterBy(null, "OPEN", null, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public EventStatisticsResponse getStatistics() {
        long total = eventRepository.count();
        long upcoming = eventRepository.findAll(EventSpecification.filterBy(null, "UPCOMING", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long ongoing = eventRepository.findAll(EventSpecification.filterBy(null, "ONGOING", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long completed = eventRepository.findAll(EventSpecification.filterBy(null, "COMPLETED", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long cancelled = eventRepository.findAll(EventSpecification.filterBy(null, "CANCELLED", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long totalRegs = registrationRepository.count();
        return EventStatisticsResponse.builder()
                .totalEvents(total)
                .upcoming(upcoming)
                .ongoing(ongoing)
                .completed(completed)
                .cancelled(cancelled)
                .totalRegistrations(totalRegs)
                .build();
    }

    private Event getActive(UUID id) {
        return eventRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    private EventDetailResponse buildDetail(Event e) {
        return EventDetailResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .slug(e.getSlug())
                .description(e.getDescription())
                .eventType(e.getEventType().name())
                .status(e.getStatus().name())
                .coverMediaId(e.getCoverMediaId())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .locationType(e.getLocationType())
                .locationDetails(e.getLocationDetails())
                .registrationDeadline(e.getRegistrationDeadline())
                .timezone(e.getTimezone())
                .meetingLink(e.getMeetingLink())
                .maxParticipants(e.getMaxParticipants())
                .isFree(e.getIsFree())
                .price(e.getPrice())
                .organizerName(e.getCreatedBy() != null ? e.getCreatedBy().getFirstName() + " " + e.getCreatedBy().getLastName() : null)
                .createdAt(e.getCreatedAt())
                .build();
    }

    private EventSummaryResponse buildSummary(Event e) {
        return EventSummaryResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .slug(e.getSlug())
                .description(e.getDescription())
                .eventType(e.getEventType().name())
                .status(e.getStatus().name())
                .coverMediaId(e.getCoverMediaId())
                .locationType(e.getLocationType())
                .locationDetails(e.getLocationDetails())
                .startTime(e.getStartTime())
                .endTime(e.getEndTime())
                .isFree(e.getIsFree())
                .price(e.getPrice())
                .maxParticipants(e.getMaxParticipants())
                .organizerName(e.getCreatedBy() != null ? e.getCreatedBy().getFirstName() + " " + e.getCreatedBy().getLastName() : null)
                .build();
    }
}
