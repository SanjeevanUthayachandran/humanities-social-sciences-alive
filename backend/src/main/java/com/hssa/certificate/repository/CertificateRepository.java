package com.hssa.certificate.repository;

import com.hssa.certificate.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID>, JpaSpecificationExecutor<Certificate> {
    Optional<Certificate> findByCertificateNumberAndDeletedAtIsNull(String certificateNumber);
    boolean existsByUserIdAndCourseIdAndDeletedAtIsNull(UUID userId, UUID courseId);
}
