package com.hssa.certificate.entity;

import com.hssa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "certificate_templates", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateTemplate extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "background_media_id")
    private UUID backgroundMediaId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "layout_json", columnDefinition = "jsonb", nullable = false)
    private String layoutJson;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
