package com.hssa.workshop.entity;

import com.hssa.auth.entity.User;
import com.hssa.workshop.enums.EventStatus;
import com.hssa.workshop.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workshops", schema = "event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workshop {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, unique = true, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    @Builder.Default
    private EventType eventType = EventType.WORKSHOP;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EventStatus status = EventStatus.UPCOMING;

    @Column(name = "cover_media_id")
    private UUID coverMediaId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "location_type", nullable = false, length = 50)
    @Builder.Default
    private String locationType = "ONLINE";

    @Column(name = "location_details", columnDefinition = "TEXT")
    private String locationDetails;

    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;

    @Column(length = 100)
    private String timezone;

    @Column(name = "meeting_link", columnDefinition = "TEXT")
    private String meetingLink;

    @Column(name = "meeting_password", length = 255)
    private String meetingPassword;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "is_free", nullable = false)
    @Builder.Default
    private Boolean isFree = true;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
