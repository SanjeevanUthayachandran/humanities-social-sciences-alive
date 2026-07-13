package com.hssa.certificate.generator;

import com.hssa.certificate.entity.Certificate;

public interface CertificateGenerator {
    byte[] generateCertificate(Certificate certificate);
}
