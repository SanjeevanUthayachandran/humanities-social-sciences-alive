package com.hssa.workshop.repository;

import com.hssa.workshop.entity.WorkshopSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkshopSessionRepository extends JpaRepository<WorkshopSession, UUID> {
    List<WorkshopSession> findByWorkshopIdOrderByStartTimeAsc(UUID workshopId);
}
