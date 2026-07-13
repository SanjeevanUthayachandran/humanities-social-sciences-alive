package com.hssa.certificate.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.certificate.dto.request.GenerateCertificateRequest;
import com.hssa.certificate.dto.request.ReissueCertificateRequest;
import com.hssa.certificate.dto.request.RevokeCertificateRequest;
import com.hssa.certificate.dto.response.CertificateResponse;
import com.hssa.certificate.dto.response.CertificateSummaryResponse;
import com.hssa.certificate.entity.Certificate;
import com.hssa.certificate.entity.CertificateVerification;
import com.hssa.certificate.enums.CertificateStatus;
import com.hssa.certificate.event.*;
import com.hssa.certificate.generator.CertificateGenerator;
import com.hssa.certificate.repository.CertificateRepository;
import com.hssa.certificate.repository.CertificateVerificationRepository;
import com.hssa.certificate.service.CertificateService;
import com.hssa.certificate.specification.CertificateSpecification;
import com.hssa.course.entity.Course;
import com.hssa.course.repository.CourseRepository;
import com.hssa.enrollment.entity.Enrollment;
import com.hssa.enrollment.enums.EnrollmentStatus;
import com.hssa.enrollment.repository.EnrollmentRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository certificateRepository;
    private final CertificateVerificationRepository verificationRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final CertificateGenerator certificateGenerator;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CertificateResponse generateCertificate(GenerateCertificateRequest request) {
        if (certificateRepository.existsByUserIdAndCourseIdAndDeletedAtIsNull(request.getUserId(), request.getCourseId())) {
            throw new BusinessException("Certificate already exists for this user and course");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseIdAndDeletedAtIsNull(user.getId(), course.getId())
                .orElseThrow(() -> new BusinessException("User is not enrolled in this course"));

        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new BusinessException("Course is not completed by the user");
        }

        String certificateNumber = generateCertificateNumber(user.getId(), course.getId());
        String verificationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

        Certificate certificate = Certificate.builder()
                .user(user)
                .course(course)
                .certificateNumber(certificateNumber)
                .status(CertificateStatus.GENERATED)
                .issuedAt(LocalDateTime.now())
                .build();
        certificate = certificateRepository.save(certificate);

        CertificateVerification verification = CertificateVerification.builder()
                .certificate(certificate)
                .verificationCode(verificationCode)
                .build();
        verificationRepository.save(verification);

        certificate.setStatus(CertificateStatus.ISSUED);
        certificate = certificateRepository.save(certificate);

        eventPublisher.publishEvent(new CertificateGeneratedEvent(this, certificate.getId()));
        eventPublisher.publishEvent(new CertificateIssuedEvent(this, certificate.getId()));

        return mapToResponse(certificate, verificationCode);
    }

    @Override
    @Transactional
    public CertificateResponse reissueCertificate(ReissueCertificateRequest request) {
        Certificate oldCert = certificateRepository.findById(request.getCertificateId())
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        
        if (oldCert.getStatus() == CertificateStatus.REVOKED) {
            throw new BusinessException("Cannot reissue a revoked certificate");
        }

        oldCert.setStatus(CertificateStatus.REVOKED);
        certificateRepository.save(oldCert);
        eventPublisher.publishEvent(new CertificateRevokedEvent(this, oldCert.getId()));

        GenerateCertificateRequest genReq = new GenerateCertificateRequest();
        genReq.setUserId(oldCert.getUser().getId());
        genReq.setCourseId(oldCert.getCourse().getId());
        
        // Temporarily bypass the "already exists" check manually
        String newCertNum = generateCertificateNumber(oldCert.getUser().getId(), oldCert.getCourse().getId()) + "-R";
        String verificationCode = UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

        Certificate newCert = Certificate.builder()
                .user(oldCert.getUser())
                .course(oldCert.getCourse())
                .certificateNumber(newCertNum)
                .status(CertificateStatus.ISSUED)
                .issuedAt(LocalDateTime.now())
                .build();
        newCert = certificateRepository.save(newCert);

        CertificateVerification verification = CertificateVerification.builder()
                .certificate(newCert)
                .verificationCode(verificationCode)
                .build();
        verificationRepository.save(verification);

        eventPublisher.publishEvent(new CertificateReissuedEvent(this, newCert.getId()));

        return mapToResponse(newCert, verificationCode);
    }

    @Override
    @Transactional
    public void revokeCertificate(UUID certificateId, RevokeCertificateRequest request) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        
        certificate.setStatus(CertificateStatus.REVOKED);
        certificateRepository.save(certificate);
        
        eventPublisher.publishEvent(new CertificateRevokedEvent(this, certificate.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse getCertificate(UUID certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        CertificateVerification verification = verificationRepository.findByCertificateIdAndDeletedAtIsNull(certificateId)
                .orElseThrow(() -> new BusinessException("Verification data not found"));
        return mapToResponse(certificate, verification.getVerificationCode());
    }

    @Override
    @Transactional
    public byte[] downloadCertificate(UUID certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        eventPublisher.publishEvent(new CertificateDownloadedEvent(this, certificate.getId()));
        return certificateGenerator.generateCertificate(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] previewCertificate(UUID certificateId) {
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate not found"));
        return certificateGenerator.generateCertificate(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CertificateSummaryResponse> searchCertificates(UUID studentId, UUID courseId, String certificateNumber, String status, Pageable pageable) {
        Page<Certificate> certificates = certificateRepository.findAll(
                CertificateSpecification.filterBy(studentId, courseId, certificateNumber, status, null, null), pageable);
        
        return certificates.map(c -> CertificateSummaryResponse.builder()
                .id(c.getId())
                .certificateNumber(c.getCertificateNumber())
                .studentName(c.getUser().getFirstName() + " " + c.getUser().getLastName())
                .courseName(c.getCourse().getTitle())
                .status(c.getStatus().name())
                .issuedAt(c.getIssuedAt())
                .build());
    }

    private String generateCertificateNumber(UUID userId, UUID courseId) {
        return "HSSA-CERT-" + System.currentTimeMillis();
    }

    private CertificateResponse mapToResponse(Certificate certificate, String verificationCode) {
        return CertificateResponse.builder()
                .id(certificate.getId())
                .userId(certificate.getUser().getId())
                .courseId(certificate.getCourse().getId())
                .certificateNumber(certificate.getCertificateNumber())
                .status(certificate.getStatus().name())
                .issuedAt(certificate.getIssuedAt())
                .verificationCode(verificationCode)
                .build();
    }
}
