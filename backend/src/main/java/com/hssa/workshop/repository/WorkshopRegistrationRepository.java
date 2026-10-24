package com.hssa.workshop.repository;

import com.hssa.workshop.entity.WorkshopRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WorkshopRegistrationRepository extends JpaRepository<WorkshopRegistration, UUID> {
    boolean existsByWorkshopIdAndUserId(UUID workshopId, UUID userId);
    Optional<WorkshopRegistration> findByWorkshopIdAndUserId(UUID workshopId, UUID userId);
    List<WorkshopRegistration> findByWorkshopId(UUID workshopId);
    List<WorkshopRegistration> findByUserIdOrderByRegisteredAtDesc(UUID userId);
    long countByWorkshopIdAndStatus(UUID workshopId, String status);
}
