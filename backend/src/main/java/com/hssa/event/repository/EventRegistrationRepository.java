package com.hssa.event.repository;

import com.hssa.event.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, UUID> {
    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);
    Optional<EventRegistration> findByEventIdAndUserId(UUID eventId, UUID userId);
    List<EventRegistration> findByEventId(UUID eventId);
    long countByEventIdAndStatus(UUID eventId, String status);
}