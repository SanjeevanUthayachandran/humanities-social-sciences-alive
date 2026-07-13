package com.hssa.publication.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.publication.enums.PublicationStatus;
import com.hssa.publication.enums.PublicationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "publications", schema = "publication")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publication extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PublicationCategory category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "publication_type", nullable = false)
    private PublicationType publicationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PublicationStatus status = PublicationStatus.DRAFT;

    @Column(name = "cover_media_id")
    private UUID coverMediaId;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @Column(length = 100)
    private String doi;
}
