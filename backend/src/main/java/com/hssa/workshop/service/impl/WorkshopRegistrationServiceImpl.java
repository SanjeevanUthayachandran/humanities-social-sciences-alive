package com.hssa.workshop.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.workshop.dto.request.AttendanceRequest;
import com.hssa.workshop.dto.response.WorkshopRegistrationResponse;
import com.hssa.workshop.entity.Workshop;
import com.hssa.workshop.entity.WorkshopAttendance;
import com.hssa.workshop.entity.WorkshopRegistration;
import com.hssa.workshop.entity.WorkshopSession;
import com.hssa.workshop.event.WorkshopAttendanceEvent;
import com.hssa.workshop.event.WorkshopRegistrationEvent;
import com.hssa.workshop.repository.WorkshopAttendanceRepository;
import com.hssa.workshop.repository.WorkshopRegistrationRepository;
import com.hssa.workshop.repository.WorkshopRepository;
import com.hssa.workshop.repository.WorkshopSessionRepository;
import com.hssa.workshop.service.WorkshopRegistrationService;
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
public class WorkshopRegistrationServiceImpl implements WorkshopRegistrationService {

    private final WorkshopRepository workshopRepository;
    private final WorkshopRegistrationRepository registrationRepository;
    private final WorkshopAttendanceRepository attendanceRepository;
    private final WorkshopSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public WorkshopRegistrationResponse register(UUID workshopId, UUID userId) {
        Workshop workshop = workshopRepository.findByIdAndDeletedAtIsNull(workshopId)
                .orElseThrow(() -> new ResourceNotFoundException("Workshop not found"));
        if (registrationRepository.existsByWorkshopIdAndUserId(workshopId, userId)) {
            throw new BusinessException("User already registered for this workshop");
        }
        if (workshop.getMaxParticipants() != null) {
            long registered = registrationRepository.countByWorkshopIdAndStatus(workshopId, "REGISTERED");
            if (registered >= workshop.getMaxParticipants()) {
                throw new BusinessException("Workshop is at full capacity");
            }
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        WorkshopRegistration reg = WorkshopRegistration.builder()
                .workshop(workshop)
                .user(user)
                .status("REGISTERED")
                .registeredAt(LocalDateTime.now())
                .build();
        reg = registrationRepository.save(reg);
        eventPublisher.publishEvent(new WorkshopRegistrationEvent(this, workshopId, userId));
        return toResponse(reg);
    }

    @Override
    @Transactional
    public void cancelRegistration(UUID workshopId, UUID userId) {
        WorkshopRegistration reg = registrationRepository.findByWorkshopIdAndUserId(workshopId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        reg.setStatus("CANCELLED");
        registrationRepository.save(reg);
    }

    @Override
    @Transactional
    public void approveRegistration(UUID workshopId, UUID userId) {
        WorkshopRegistration reg = registrationRepository.findByWorkshopIdAndUserId(workshopId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        reg.setStatus("APPROVED");
        registrationRepository.save(reg);
    }

    @Override
    @Transactional
    public void recordAttendance(UUID workshopId, AttendanceRequest request, UUID recordedBy) {
        WorkshopRegistration reg = registrationRepository.findById(request.getRegistrationId())
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found"));
        WorkshopSession session = null;
        if (request.getSessionId() != null) {
            session = sessionRepository.findById(request.getSessionId()).orElse(null);
        }
        User recorder = userRepository.findById(recordedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        WorkshopAttendance attendance = WorkshopAttendance.builder()
                .registration(reg)
                .session(session)
                .joinedAt(request.getJoinedAt() != null ? request.getJoinedAt() : LocalDateTime.now())
                .leftAt(request.getLeftAt())
                .durationMinutes(request.getDurationMinutes())
                .createdBy(recorder)
                .build();
        attendanceRepository.save(attendance);
        eventPublisher.publishEvent(new WorkshopAttendanceEvent(this, workshopId, reg.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopRegistrationResponse> getRegistrations(UUID workshopId) {
        return registrationRepository.findByWorkshopId(workshopId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkshopRegistrationResponse> getUserRegistrations(UUID userId) {
        return registrationRepository.findByUserIdOrderByRegisteredAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private WorkshopRegistrationResponse toResponse(WorkshopRegistration r) {
        return WorkshopRegistrationResponse.builder()
                .id(r.getId())
                .workshopId(r.getWorkshop().getId())
                .workshopTitle(r.getWorkshop().getTitle())
                .userId(r.getUser().getId())
                .userFullName(r.getUser().getFirstName() + " " + r.getUser().getLastName())
                .status(r.getStatus())
                .registeredAt(r.getRegisteredAt())
                .startTime(r.getWorkshop().getStartTime())
                .build();
    }
}
