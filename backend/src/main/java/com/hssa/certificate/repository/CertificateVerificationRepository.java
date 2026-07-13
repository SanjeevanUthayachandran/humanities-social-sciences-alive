package com.hssa.certificate.repository;

import com.hssa.certificate.entity.CertificateVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CertificateVerificationRepository extends JpaRepository<CertificateVerification, UUID> {
    Optional<CertificateVerification> findByVerificationCodeAndDeletedAtIsNull(String verificationCode);
    Optional<CertificateVerification> findByCertificateIdAndDeletedAtIsNull(UUID certificateId);
}
