package com.hssa.workshop.repository;

import com.hssa.workshop.entity.WorkshopSpeaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkshopSpeakerRepository extends JpaRepository<WorkshopSpeaker, UUID> {
    List<WorkshopSpeaker> findByWorkshopId(UUID workshopId);
}
