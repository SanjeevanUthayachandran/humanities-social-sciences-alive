package com.hssa.payment.entity;

import com.hssa.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "course_payment_settings", schema = "payment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePaymentSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, unique = true)
    private Course course;

    @Column(name = "gateway_code", nullable = false, length = 30)
    @Builder.Default
    private String gatewayCode = "PAYHERE";

    @Column(name = "online_payment_enabled", nullable = false)
    @Builder.Default
    private boolean onlinePaymentEnabled = true;

    @Column(name = "bank_transfer_enabled", nullable = false)
    @Builder.Default
    private boolean bankTransferEnabled = false;

    @Column(name = "installments_enabled", nullable = false)
    @Builder.Default
    private boolean installmentsEnabled = false;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @Version
    private Integer version;
}
