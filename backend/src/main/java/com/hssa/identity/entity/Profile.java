package com.hssa.identity.entity;

import com.hssa.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "profiles", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "profile_image_media_id")
    private UUID profileImageMediaId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "biography", columnDefinition = "TEXT")
    private String biography;

    @Column(name = "nationality", length = 100)
    private String nationality;

    @Column(name = "preferred_language", length = 50)
    private String preferredLanguage = "English";

    @Column(name = "timezone", length = 100)
    private String timezone = "Asia/Colombo";

    @Column(name = "website", columnDefinition = "TEXT")
    private String website;

    @Column(name = "linkedin", columnDefinition = "TEXT")
    private String linkedin;

    @Column(name = "github", columnDefinition = "TEXT")
    private String github;

    @Column(name = "google_scholar", columnDefinition = "TEXT")
    private String googleScholar;

    @Column(name = "researchgate", columnDefinition = "TEXT")
    private String researchgate;

    @Column(name = "orcid", length = 100)
    private String orcid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
