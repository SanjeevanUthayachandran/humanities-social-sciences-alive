package com.hssa.config;

import com.hssa.security.dto.CustomUserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
     * Anonymous operations such as account registration have no existing user,
     * so their audit columns remain null. Authenticated operations use the ID
     * carried by the application's security principal.
     *
     * @return the authenticated user's UUID, or empty for anonymous requests.
     */
    @Bean
    public AuditorAware<UUID> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.empty();
            }

            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails && userDetails.getId() != null) {
                return Optional.of(userDetails.getId());
            }

            return Optional.empty();
        };
    }
}
