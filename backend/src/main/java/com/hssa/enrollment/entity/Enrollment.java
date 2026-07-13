package com.hssa.enrollment.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.course.entity.Course;
import com.hssa.enrollment.enums.EnrollmentStatus;
import com.hssa.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.PENDING;

    @Column(name = "enrolled_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime enrolledAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
