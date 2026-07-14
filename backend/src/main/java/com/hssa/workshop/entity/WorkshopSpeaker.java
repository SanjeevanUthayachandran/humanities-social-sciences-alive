package com.hssa.workshop.entity;

import com.hssa.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "workshop_speakers", schema = "event",
    uniqueConstraints = @UniqueConstraint(columnNames = {"workshop_id", "speaker_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkshopSpeaker {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workshop_id", nullable = false)
    private Workshop workshop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id", nullable = false)
    private User speaker;

    @Column(name = "is_keynote")
    @Builder.Default
    private Boolean isKeynote = false;

    @Column(length = 255)
    private String bio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
