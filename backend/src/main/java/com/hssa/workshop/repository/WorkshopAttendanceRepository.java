package com.hssa.workshop.repository;

import com.hssa.workshop.entity.WorkshopAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkshopAttendanceRepository extends JpaRepository<WorkshopAttendance, UUID> {
    List<WorkshopAttendance> findByRegistrationId(UUID registrationId);
}
