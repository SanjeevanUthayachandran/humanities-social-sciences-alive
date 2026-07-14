package com.hssa.notification.entity;

import com.hssa.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_notification_settings", schema = "notification",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationSetting {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email_enabled")
    @Builder.Default
    private Boolean emailEnabled = true;

    @Column(name = "push_enabled")
    @Builder.Default
    private Boolean pushEnabled = true;

    @Column(name = "sms_enabled")
    @Builder.Default
    private Boolean smsEnabled = false;

    @Column(name = "course_notifications")
    @Builder.Default
    private Boolean courseNotifications = true;

    @Column(name = "payment_notifications")
    @Builder.Default
    private Boolean paymentNotifications = true;

    @Column(name = "certificate_notifications")
    @Builder.Default
    private Boolean certificateNotifications = true;

    @Column(name = "workshop_notifications")
    @Builder.Default
    private Boolean workshopNotifications = true;

    @Column(name = "event_notifications")
    @Builder.Default
    private Boolean eventNotifications = true;

    @Column(name = "news_notifications")
    @Builder.Default
    private Boolean newsNotifications = true;

    @Column(name = "announcement_notifications")
    @Builder.Default
    private Boolean announcementNotifications = true;

    @Column(name = "system_notifications")
    @Builder.Default
    private Boolean systemNotifications = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}