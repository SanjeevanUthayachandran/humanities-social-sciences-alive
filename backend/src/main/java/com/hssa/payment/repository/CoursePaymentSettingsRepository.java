package com.hssa.payment.repository;

import com.hssa.payment.entity.CoursePaymentSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CoursePaymentSettingsRepository extends JpaRepository<CoursePaymentSettings, UUID> {
    Optional<CoursePaymentSettings> findByCourse_Id(UUID courseId);
}
