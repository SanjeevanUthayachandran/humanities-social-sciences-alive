package com.hssa.certificate.repository;

import com.hssa.certificate.entity.CertificateTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CertificateTemplateRepository extends JpaRepository<CertificateTemplate, UUID> {
}
