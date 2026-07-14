package com.hssa.workshop.repository;

import com.hssa.workshop.entity.WorkshopFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkshopFeedbackRepository extends JpaRepository<WorkshopFeedback, UUID> {
    List<WorkshopFeedback> findByWorkshopId(UUID workshopId);
    boolean existsByWorkshopIdAndUserId(UUID workshopId, UUID userId);
    Optional<WorkshopFeedback> findByWorkshopIdAndUserId(UUID workshopId, UUID userId);
}
