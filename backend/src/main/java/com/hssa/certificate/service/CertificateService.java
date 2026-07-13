package com.hssa.certificate.service;

import com.hssa.certificate.dto.request.GenerateCertificateRequest;
import com.hssa.certificate.dto.request.ReissueCertificateRequest;
import com.hssa.certificate.dto.request.RevokeCertificateRequest;
import com.hssa.certificate.dto.response.CertificateResponse;
import com.hssa.certificate.dto.response.CertificateSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CertificateService {
    CertificateResponse generateCertificate(GenerateCertificateRequest request);
    CertificateResponse reissueCertificate(ReissueCertificateRequest request);
    void revokeCertificate(UUID certificateId, RevokeCertificateRequest request);
    CertificateResponse getCertificate(UUID certificateId);
    byte[] downloadCertificate(UUID certificateId);
    byte[] previewCertificate(UUID certificateId);
    Page<CertificateSummaryResponse> searchCertificates(UUID studentId, UUID courseId, String certificateNumber, String status, Pageable pageable);
}
