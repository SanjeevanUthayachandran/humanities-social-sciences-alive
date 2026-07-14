package com.hssa.event.repository;

import com.hssa.event.entity.EventGallery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventGalleryRepository extends JpaRepository<EventGallery, UUID> {
    List<EventGallery> findByEventId(UUID eventId);
}