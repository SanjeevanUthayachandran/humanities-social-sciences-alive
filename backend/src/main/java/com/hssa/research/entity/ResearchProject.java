package com.hssa.research.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.research.enums.ResearchStatus;
import com.hssa.research.enums.ResearchType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "research_projects", schema = "research")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchProject extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ResearchCategory category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "research_type", nullable = false)
    private ResearchType researchType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ResearchStatus status = ResearchStatus.DRAFT;

    @Column(name = "cover_media_id")
    private UUID coverMediaId;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;
}
