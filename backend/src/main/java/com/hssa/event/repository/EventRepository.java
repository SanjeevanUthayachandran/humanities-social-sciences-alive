package com.hssa.event.repository;

import com.hssa.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByIdAndDeletedAtIsNull(UUID id);
    boolean existsBySlugAndDeletedAtIsNull(String slug);
}