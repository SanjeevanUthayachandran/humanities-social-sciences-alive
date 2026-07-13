package com.hssa.lesson.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Module extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
