package com.hssa.auth.service.impl;

import com.hssa.auth.dto.request.*;
import com.hssa.auth.dto.response.AuthenticationResponse;
import com.hssa.auth.dto.response.TokenResponse;
import com.hssa.auth.dto.response.UserSummaryResponse;
import com.hssa.auth.entity.PasswordResetToken;
import com.hssa.auth.entity.RefreshToken;
import com.hssa.auth.entity.Role;
import com.hssa.auth.entity.User;
import com.hssa.auth.enums.AccountStatus;
import com.hssa.auth.enums.AuthProvider;
import com.hssa.auth.repository.PasswordResetTokenRepository;
import com.hssa.auth.repository.RefreshTokenRepository;
import com.hssa.auth.repository.RoleRepository;
import com.hssa.auth.repository.UserRepository;
import com.hssa.auth.service.AuthService;
import com.hssa.auth.service.JwtService;
import com.hssa.auth.service.GoogleIdentityService;
import com.hssa.auth.service.PasswordResetMailService;
import com.hssa.auth.service.RefreshTokenService;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.security.dto.CustomUserDetails;
import com.hssa.security.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetMailService passwordResetMailService;
    private final CustomUserDetailsService userDetailsService;
    private final GoogleIdentityService googleIdentityService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException("Email already exists");
        }

        Role userRole = roleRepository.findByRoleCode("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
        User user = new User();
        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setDisplayName((request.getFirstName() + " " + request.getLastName()).trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setActive(true);
        user.getRoles().add(userRole);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AuthenticationResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        String email = normalizeEmail(request.getEmail());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(email);
        String jwtToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .user(toUserSummary(user))
                .build();
    }

    @Override
    @Transactional
    public AuthenticationResponse googleLogin(GoogleLoginRequest request) {
        GoogleIdentityService.GoogleProfile profile = googleIdentityService.verify(request.getCredential());
        User user = userRepository.findByEmail(profile.email()).orElseGet(() -> createGoogleUser(profile));

        if (user.getAuthProvider() == AuthProvider.LOCAL && !profile.authoritativeEmail()) {
            throw new BusinessException("Use your email and password to sign in to this existing account");
        }
        if (!user.isActive() || user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new BusinessException("This account is not active");
        }

        user.setEmailVerified(true);
        if (user.getEmailVerifiedAt() == null) {
            user.setEmailVerifiedAt(LocalDateTime.now());
        }
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.saveAndFlush(user);

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .user(toUserSummary(userRepository.findByEmail(user.getEmail()).orElseThrow()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndIsRevokedFalse(request.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new BusinessException("Refresh token is invalid"));
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService
                .loadUserByUsername(refreshToken.getUser().getEmail());
        return TokenResponse.builder()
                .accessToken(jwtService.generateToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Override
    public void logout(String sessionToken) {
        // Access tokens are stateless. Password changes revoke all refresh tokens.
    }

    @Override
    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        passwordResetMailService.ensureConfigured();
        userRepository.findByEmail(normalizeEmail(request.getEmail())).ifPresent(user -> {
            passwordResetTokenRepository.deleteByUser_IdAndUsedAtIsNull(user.getId());
            String rawToken = generateResetToken();
            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setUser(user);
            resetToken.setToken(hashResetToken(rawToken));
            resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            passwordResetTokenRepository.save(resetToken);
            passwordResetMailService.sendPasswordReset(user.getEmail(), rawToken);
        });
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(hashResetToken(request.getToken()))
                .orElseThrow(() -> new BusinessException("Password reset link is invalid"));
        if (resetToken.getUsedAt() != null || resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Password reset link has expired or has already been used");
        }
        User user = resetToken.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        resetToken.setUsedAt(LocalDateTime.now());
        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);
        refreshTokenService.revokeAllUserTokens(user.getId());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request, String email) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new BusinessException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        refreshTokenService.revokeAllUserTokens(user.getId());
    }

    @Override
    public void verifyEmail(VerifyEmailRequest request) {
        throw new BusinessException("Email verification is not configured yet");
    }

    private UserSummaryResponse toUserSummary(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .roles(user.getRoles().stream().map(Role::getRoleCode).collect(Collectors.toSet()))
                .permissions(user.getRoles().stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .map(permission -> permission.getPermissionCode())
                        .collect(Collectors.toCollection(HashSet::new)))
                .build();
    }

    private User createGoogleUser(GoogleIdentityService.GoogleProfile profile) {
        Role userRole = roleRepository.findByRoleCode("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role USER not found"));
        String firstName = valueOrFallback(profile.givenName(), "Google");
        String lastName = valueOrFallback(profile.familyName(), "User");

        User user = new User();
        user.setEmail(profile.email());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setDisplayName(valueOrFallback(profile.displayName(), firstName + " " + lastName));
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setActive(true);
        user.getRoles().add(userRole);
        return userRepository.saveAndFlush(user);
    }

    private String valueOrFallback(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private String generateResetToken() {
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashResetToken(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }
}
