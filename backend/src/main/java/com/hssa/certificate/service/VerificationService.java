package com.hssa.certificate.service;

import com.hssa.certificate.dto.response.CertificateVerificationResponse;

public interface VerificationService {
    CertificateVerificationResponse verifyCertificateByNumber(String certificateNumber);
    CertificateVerificationResponse verifyCertificateByCode(String verificationCode);
}
