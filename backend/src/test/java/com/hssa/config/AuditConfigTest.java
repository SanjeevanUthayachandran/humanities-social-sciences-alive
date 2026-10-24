package com.hssa.config;

import com.hssa.security.dto.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuditConfigTest {

    private final AuditorAware<UUID> auditorAware = new AuditConfig().auditorProvider();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void anonymousRequestHasNoAuditor() {
        assertTrue(auditorAware.getCurrentAuditor().isEmpty());
    }

    @Test
    void authenticatedRequestUsesPrincipalId() {
        UUID userId = UUID.randomUUID();
        CustomUserDetails principal = CustomUserDetails.builder()
                .id(userId)
                .username("person@example.com")
                .email("person@example.com")
                .authorities(List.of())
                .build();
        SecurityContextHolder.getContext().setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(principal, null, List.of()));

        assertEquals(userId, auditorAware.getCurrentAuditor().orElseThrow());
    }
}
