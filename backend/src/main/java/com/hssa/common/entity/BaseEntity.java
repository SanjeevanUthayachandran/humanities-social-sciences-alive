package com.hssa.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Abstract base entity providing common fields for all database entities.
 * Includes primary key generation, auditing timestamps, auditing user IDs,
 * and optimistic locking versioning.
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    /**
     * Unique identifier for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    /**
     * Timestamp when the entity was created.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * UUID of the user who created the entity.
     */
    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    /**
     * UUID of the user who last updated the entity.
     */
    @LastModifiedBy
    @Column
    private UUID updatedBy;

    /**
     * Timestamp when the entity was soft deleted. Null if active.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Version for optimistic locking.
     */
    @Version
    private Integer version;
}
