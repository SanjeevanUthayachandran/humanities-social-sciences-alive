package com.hssa.course.entity;

import com.hssa.common.entity.BaseEntity;
import com.hssa.course.enums.CourseLevel;
import com.hssa.course.enums.CourseStatus;
import com.hssa.course.enums.CourseVisibility;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "courses", schema = "lms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory category;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, length = 255, unique = true)
    private String slug;

    @Column(length = 500)
    private String subtitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    @Builder.Default
    private String language = "English";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CourseLevel level = CourseLevel.BEGINNER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CourseVisibility visibility = CourseVisibility.PUBLIC;

    @Column(name = "thumbnail_media_id")
    private UUID thumbnailMediaId;

    @Column(name = "banner_media_id")
    private UUID bannerMediaId;

    @Column(name = "promotional_video_id")
    private UUID promotionalVideoId;

    @Column(name = "is_free", nullable = false)
    @Builder.Default
    private Boolean isFree = false;

    private BigDecimal price;

    @Column(length = 3)
    @Builder.Default
    private String currency = "LKR";

    @Column(name = "discounted_price")
    private BigDecimal discountedPrice;

    @Column(name = "duration_hours")
    private BigDecimal durationHours;

    @Column(name = "certification_available", nullable = false)
    @Builder.Default
    private Boolean certificationAvailable = true;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "meta_title", length = 255)
    private String metaTitle;

    @Column(name = "meta_description", columnDefinition = "TEXT")
    private String metaDescription;

    @Column(name = "meta_keywords", columnDefinition = "TEXT")
    private String metaKeywords;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "course_tag_map",
            schema = "lms",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private Set<CourseTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CourseInstructor> instructors = new HashSet<>();
    
    public void addInstructor(CourseInstructor instructor) {
        instructors.add(instructor);
        instructor.setCourse(this);
    }
}
