package com.hssa.workshop.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.workshop.dto.request.CreateWorkshopRequest;
import com.hssa.workshop.dto.request.UpdateWorkshopRequest;
import com.hssa.workshop.dto.response.WorkshopDetailResponse;
import com.hssa.workshop.dto.response.WorkshopStatisticsResponse;
import com.hssa.workshop.dto.response.WorkshopSummaryResponse;
import com.hssa.workshop.entity.Workshop;
import com.hssa.workshop.enums.EventStatus;
import com.hssa.workshop.event.*;
import com.hssa.workshop.repository.WorkshopRegistrationRepository;
import com.hssa.workshop.repository.WorkshopRepository;
import com.hssa.workshop.service.WorkshopService;
import com.hssa.workshop.specification.WorkshopSpecification;
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
public class WorkshopServiceImpl implements WorkshopService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopRegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public WorkshopDetailResponse createWorkshop(CreateWorkshopRequest request, UUID creatorId) {
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String slug = request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + System.currentTimeMillis();
        Workshop workshop = Workshop.builder()
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
        workshop = workshopRepository.save(workshop);
        eventPublisher.publishEvent(new WorkshopCreatedEvent(this, workshop.getId()));
        return buildDetail(workshop);
    }

    @Override
    @Transactional
    public WorkshopDetailResponse updateWorkshop(UUID id, UpdateWorkshopRequest request, UUID updaterId) {
        Workshop workshop = workshopRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        User updater = userRepository.findById(updaterId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (request.getTitle() != null) workshop.setTitle(request.getTitle());
        if (request.getDescription() != null) workshop.setDescription(request.getDescription());
        if (request.getEventType() != null) workshop.setEventType(request.getEventType());
        if (request.getCoverMediaId() != null) workshop.setCoverMediaId(request.getCoverMediaId());
        if (request.getStartTime() != null) workshop.setStartTime(request.getStartTime());
        if (request.getEndTime() != null) workshop.setEndTime(request.getEndTime());
        if (request.getLocationType() != null) workshop.setLocationType(request.getLocationType());
        if (request.getLocationDetails() != null) workshop.setLocationDetails(request.getLocationDetails());
        if (request.getRegistrationDeadline() != null) workshop.setRegistrationDeadline(request.getRegistrationDeadline());
        if (request.getTimezone() != null) workshop.setTimezone(request.getTimezone());
        if (request.getMeetingLink() != null) workshop.setMeetingLink(request.getMeetingLink());
        if (request.getMeetingPassword() != null) workshop.setMeetingPassword(request.getMeetingPassword());
        if (request.getMaxParticipants() != null) workshop.setMaxParticipants(request.getMaxParticipants());
        if (request.getIsFree() != null) workshop.setIsFree(request.getIsFree());
        if (request.getPrice() != null) workshop.setPrice(request.getPrice());
        workshop.setUpdatedBy(updater);
        workshop = workshopRepository.save(workshop);
        eventPublisher.publishEvent(new WorkshopUpdatedEvent(this, workshop.getId()));
        return buildDetail(workshop);
    }

    @Override
    @Transactional
    public void deleteWorkshop(UUID id) {
        Workshop w = workshopRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        w.setDeletedAt(LocalDateTime.now());
        workshopRepository.save(w);
    }

    @Override
    @Transactional
    public void restoreWorkshop(UUID id) {
        Workshop w = workshopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        w.setDeletedAt(null);
        workshopRepository.save(w);
    }

    @Override
    @Transactional
    public void publishWorkshop(UUID id) {
        Workshop w = getActive(id);
        w.setStatus(EventStatus.OPEN);
        workshopRepository.save(w);
        eventPublisher.publishEvent(new WorkshopPublishedEvent(this, id));
    }

    @Override
    @Transactional
    public void cancelWorkshop(UUID id) {
        Workshop w = getActive(id);
        w.setStatus(EventStatus.CANCELLED);
        workshopRepository.save(w);
        eventPublisher.publishEvent(new WorkshopCancelledEvent(this, id));
    }

    @Override
    @Transactional
    public void completeWorkshop(UUID id) {
        Workshop w = getActive(id);
        w.setStatus(EventStatus.COMPLETED);
        workshopRepository.save(w);
        eventPublisher.publishEvent(new WorkshopCompletedEvent(this, id));
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopDetailResponse getWorkshop(UUID id) {
        return buildDetail(getActive(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopSummaryResponse> listWorkshops(Pageable pageable) {
        return workshopRepository.findAll(WorkshopSpecification.filterBy(null, null, null, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopSummaryResponse> searchWorkshops(String title, String status, String eventType, String locationType, Boolean isFree, Pageable pageable) {
        return workshopRepository.findAll(WorkshopSpecification.filterBy(title, status, eventType, locationType, isFree, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkshopSummaryResponse> getFeatured(Pageable pageable) {
        return workshopRepository.findAll(WorkshopSpecification.filterBy(null, "OPEN", null, null, null, null), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkshopStatisticsResponse getStatistics() {
        long total = workshopRepository.count();
        long upcoming = workshopRepository.findAll(WorkshopSpecification.filterBy(null, "UPCOMING", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long ongoing = workshopRepository.findAll(WorkshopSpecification.filterBy(null, "ONGOING", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long completed = workshopRepository.findAll(WorkshopSpecification.filterBy(null, "COMPLETED", null, null, null, null), Pageable.unpaged()).getTotalElements();
        long cancelled = workshopRepository.findAll(WorkshopSpecification.filterBy(null, "CANCELLED", null, null, null, null), Pageable.unpaged()).getTotalElements();
        return WorkshopStatisticsResponse.builder()
                .totalWorkshops(total)
                .upcoming(upcoming)
                .ongoing(ongoing)
                .completed(completed)
                .cancelled(cancelled)
                .build();
    }

    private Workshop getActive(UUID id) {
        return workshopRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
    }

    private WorkshopDetailResponse buildDetail(Workshop w) {
        return WorkshopDetailResponse.builder()
                .id(w.getId())
                .title(w.getTitle())
                .slug(w.getSlug())
                .description(w.getDescription())
                .eventType(w.getEventType().name())
                .status(w.getStatus().name())
                .coverMediaId(w.getCoverMediaId())
                .startTime(w.getStartTime())
                .endTime(w.getEndTime())
                .locationType(w.getLocationType())
                .locationDetails(w.getLocationDetails())
                .registrationDeadline(w.getRegistrationDeadline())
                .timezone(w.getTimezone())
                .meetingLink(w.getMeetingLink())
                .maxParticipants(w.getMaxParticipants())
                .isFree(w.getIsFree())
                .price(w.getPrice())
                .organizerName(w.getCreatedBy() != null ? w.getCreatedBy().getFirstName() + " " + w.getCreatedBy().getLastName() : null)
                .createdAt(w.getCreatedAt())
                .build();
    }

    private WorkshopSummaryResponse buildSummary(Workshop w) {
        return WorkshopSummaryResponse.builder()
                .id(w.getId())
                .title(w.getTitle())
                .slug(w.getSlug())
                .description(w.getDescription())
                .eventType(w.getEventType().name())
                .status(w.getStatus().name())
                .coverMediaId(w.getCoverMediaId())
                .locationType(w.getLocationType())
                .locationDetails(w.getLocationDetails())
                .startTime(w.getStartTime())
                .endTime(w.getEndTime())
                .isFree(w.getIsFree())
                .price(w.getPrice())
                .maxParticipants(w.getMaxParticipants())
                .organizerName(w.getCreatedBy() != null ? w.getCreatedBy().getFirstName() + " " + w.getCreatedBy().getLastName() : null)
                .build();
    }
}
