package com.hssa.certificate.entity;

import com.hssa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "certificate_verification", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateVerification extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "certificate_id", nullable = false, unique = true)
    private Certificate certificate;

    @Column(name = "verification_code", nullable = false, unique = true, length = 100)
    private String verificationCode;

    @Column(name = "qr_code_media_id")
    private UUID qrCodeMediaId;
}
