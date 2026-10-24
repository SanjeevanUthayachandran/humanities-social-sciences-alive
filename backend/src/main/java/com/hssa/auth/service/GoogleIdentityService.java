package com.hssa.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.hssa.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Locale;

@Service
public class GoogleIdentityService {

    private final String clientId;
    private volatile GoogleIdTokenVerifier verifier;

    public GoogleIdentityService(@Value("${application.security.google.client-id:}") String clientId) {
        this.clientId = clientId == null ? "" : clientId.trim();
    }

    public GoogleProfile verify(String credential) {
        if (clientId.isBlank()) {
            throw new BusinessException("Google sign-in is not configured");
        }

        try {
            GoogleIdToken token = getVerifier().verify(credential);
            if (token == null) {
                throw new BusinessException("Google sign-in could not be verified");
            }

            GoogleIdToken.Payload payload = token.getPayload();
            String email = payload.getEmail();
            if (email == null || email.isBlank() || !Boolean.TRUE.equals(payload.getEmailVerified())) {
                throw new BusinessException("Google account email is not verified");
            }

            String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
            boolean googleAuthoritative = normalizedEmail.endsWith("@gmail.com")
                    || (payload.getHostedDomain() != null && !payload.getHostedDomain().isBlank());

            return new GoogleProfile(
                    payload.getSubject(),
                    normalizedEmail,
                    textClaim(payload, "given_name"),
                    textClaim(payload, "family_name"),
                    textClaim(payload, "name"),
                    googleAuthoritative);
        } catch (GeneralSecurityException | IOException exception) {
            throw new BusinessException("Google sign-in is temporarily unavailable");
        }
    }

    private GoogleIdTokenVerifier getVerifier() throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier current = verifier;
        if (current == null) {
            synchronized (this) {
                current = verifier;
                if (current == null) {
                    current = new GoogleIdTokenVerifier.Builder(
                            GoogleNetHttpTransport.newTrustedTransport(),
                            GsonFactory.getDefaultInstance())
                            .setAudience(List.of(clientId))
                            .build();
                    verifier = current;
                }
            }
        }
        return current;
    }

    private String textClaim(GoogleIdToken.Payload payload, String name) {
        Object value = payload.get(name);
        return value == null ? null : value.toString().trim();
    }

    public record GoogleProfile(
            String subject,
            String email,
            String givenName,
            String familyName,
            String displayName,
            boolean authoritativeEmail) {
    }
}
