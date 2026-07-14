package com.hssa.event.repository;

import com.hssa.event.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, UUID> {
    List<EventAttendance> findByRegistrationId(UUID registrationId);
}