package com.hssa.certificate.service.impl;

import com.hssa.certificate.dto.response.CertificateVerificationResponse;
import com.hssa.certificate.entity.Certificate;
import com.hssa.certificate.entity.CertificateVerification;
import com.hssa.certificate.enums.CertificateStatus;
import com.hssa.certificate.event.CertificateVerifiedEvent;
import com.hssa.certificate.repository.CertificateRepository;
import com.hssa.certificate.repository.CertificateVerificationRepository;
import com.hssa.certificate.service.VerificationService;
import com.hssa.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final CertificateRepository certificateRepository;
    private final CertificateVerificationRepository verificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CertificateVerificationResponse verifyCertificateByNumber(String certificateNumber) {
        Certificate certificate = certificateRepository.findByCertificateNumberAndDeletedAtIsNull(certificateNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        
        return buildResponseAndPublishEvent(certificate);
    }

    @Override
    @Transactional
    public CertificateVerificationResponse verifyCertificateByCode(String verificationCode) {
        CertificateVerification verification = verificationRepository.findByVerificationCodeAndDeletedAtIsNull(verificationCode)
                .orElseThrow(() -> new ResourceNotFoundException("Verification code not found"));
        
        Certificate certificate = verification.getCertificate();
        if (certificate.getDeletedAt() != null) {
            throw new ResourceNotFoundException("Certificate not found");
        }
        
        return buildResponseAndPublishEvent(certificate);
    }

    private CertificateVerificationResponse buildResponseAndPublishEvent(Certificate certificate) {
        eventPublisher.publishEvent(new CertificateVerifiedEvent(this, certificate.getId()));

        boolean isValid = certificate.getStatus() != CertificateStatus.REVOKED;

        return CertificateVerificationResponse.builder()
                .status(certificate.getStatus().name())
                .studentName(certificate.getUser().getFirstName() + " " + certificate.getUser().getLastName())
                .courseName(certificate.getCourse().getTitle())
                .issueDate(certificate.getIssuedAt())
                .isValid(isValid)
                .build();
    }
}
