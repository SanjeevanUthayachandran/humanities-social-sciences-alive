package com.hssa.auth.repository;

import com.hssa.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenAndIsRevokedFalse(String token);
    List<RefreshToken> findByUser_IdAndIsRevokedFalse(UUID userId);
}
