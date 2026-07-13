package com.hssa.lesson.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.lesson.enums.ResourceType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lesson_resources", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonResource extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(nullable = false, length = 255)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "resource_type", nullable = false)
    private ResourceType resourceType;

    @Column(name = "media_id")
    private UUID mediaId;

    @Column(name = "external_link", columnDefinition = "TEXT")
    private String externalLink;

    @Column(name = "is_downloadable", nullable = false)
    @Builder.Default
    private Boolean isDownloadable = true;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
