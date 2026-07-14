package com.hssa.notification.repository;

import com.hssa.notification.entity.UserNotificationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserNotificationSettingRepository extends JpaRepository<UserNotificationSetting, UUID> {
    Optional<UserNotificationSetting> findByUserId(UUID userId);
}