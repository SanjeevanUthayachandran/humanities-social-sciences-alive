package com.hssa.event.repository;

import com.hssa.event.entity.EventFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventFeedbackRepository extends JpaRepository<EventFeedback, UUID> {
    List<EventFeedback> findByEventId(UUID eventId);
    boolean existsByEventIdAndUserId(UUID eventId, UUID userId);
    Optional<EventFeedback> findByEventIdAndUserId(UUID eventId, UUID userId);
}