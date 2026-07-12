package com.hssa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

/**
 * Configuration class for JPA Auditing.
 * Enables automatic population of @CreatedDate, @LastModifiedDate,
 * @CreatedBy, and @LastModifiedBy fields.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    /**
     * Provides the current auditor (user ID).
     * Currently returns a placeholder UUID. Once Spring Security is implemented,
     * this should extract the user ID from the SecurityContextHolder.
     *
     * @return an Optional containing the UUID of the current user.
     */
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }
}
