package com.hssa.lesson.entity;

import com.hssa.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lesson_videos", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonVideo extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false, unique = true)
    private Lesson lesson;

    @Column(name = "media_id", nullable = false)
    private UUID mediaId;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "is_downloadable", nullable = false)
    @Builder.Default
    private Boolean isDownloadable = false;
}
