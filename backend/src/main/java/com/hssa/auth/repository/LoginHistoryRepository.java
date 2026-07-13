package com.hssa.auth.repository;

import com.hssa.auth.entity.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, UUID> {
    List<LoginHistory> findByUserId(UUID userId);
}
