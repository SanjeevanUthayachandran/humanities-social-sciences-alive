package com.hssa.event.repository;

import com.hssa.event.entity.EventSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventSpeakerRepository extends JpaRepository<EventSpeaker, UUID> {
    List<EventSpeaker> findByEventId(UUID eventId);
}