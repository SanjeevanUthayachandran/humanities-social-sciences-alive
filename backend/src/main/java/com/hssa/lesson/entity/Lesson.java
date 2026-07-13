package com.hssa.lesson.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.lesson.enums.LessonType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "lesson_type", nullable = false)
    private LessonType lessonType;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "is_free_preview", nullable = false)
    @Builder.Default
    private Boolean isFreePreview = false;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
