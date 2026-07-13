package com.hssa.identity.service.impl;

import com.hssa.auth.entity.LoginHistory;
import com.hssa.auth.entity.User;
import com.hssa.auth.entity.UserSession;
import com.hssa.auth.repository.LoginHistoryRepository;
import com.hssa.auth.repository.UserRepository;
import com.hssa.auth.repository.UserSessionRepository;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.exception.UnauthorizedException;
import com.hssa.identity.dto.request.UpdatePhotoRequest;
import com.hssa.identity.dto.request.UpdatePreferencesRequest;
import com.hssa.identity.dto.request.UpdatePrivacyRequest;
import com.hssa.identity.dto.request.UpdateProfileRequest;
import com.hssa.identity.dto.response.*;
import com.hssa.identity.entity.Profile;
import com.hssa.identity.entity.SocialLink;
import com.hssa.identity.entity.UserAddress;
import com.hssa.identity.entity.UserPreference;
import com.hssa.identity.mapper.ProfileMapper;
import com.hssa.identity.repository.ProfileRepository;
import com.hssa.identity.repository.SocialLinkRepository;
import com.hssa.identity.repository.UserAddressRepository;
import com.hssa.identity.repository.UserPreferenceRepository;
import com.hssa.identity.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final UserAddressRepository addressRepository;
    private final SocialLinkRepository socialLinkRepository;
    private final UserPreferenceRepository preferenceRepository;
    private final UserSessionRepository sessionRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final ProfileMapper profileMapper;

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfile(String email) {
        User user = getUserByEmail(email);
        Profile profile = profileRepository.findByUserId(user.getId()).orElse(null);
        List<UserAddress> addresses = addressRepository.findByUserId(user.getId());
        List<SocialLink> socialLinks = socialLinkRepository.findByUserId(user.getId());

        return profileMapper.toProfileResponse(user, profile, addresses, socialLinks);
    }

    @Override
    @Transactional
    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {
        User user = getUserByEmail(email);
        
        // Update User basic details
        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getMiddleName() != null) user.setMiddleName(request.getMiddleName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getDisplayName() != null) user.setDisplayName(request.getDisplayName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getBiography() != null) user.setBiography(request.getBiography());
        
        if (request.getWebsite() != null) user.setWebsite(request.getWebsite());
        if (request.getLinkedin() != null) user.setLinkedin(request.getLinkedin());
        if (request.getGoogleScholar() != null) user.setGoogleScholar(request.getGoogleScholar());
        if (request.getResearchgate() != null) user.setResearchgate(request.getResearchgate());
        if (request.getOrcid() != null) user.setOrcid(request.getOrcid());

        userRepository.save(user);

        // Update Profile
        Profile profile = profileRepository.findByUserId(user.getId()).orElseGet(() -> {
            Profile p = new Profile();
            p.setUser(user);
            return p;
        });

        if (request.getTitle() != null) profile.setTitle(request.getTitle());
        if (request.getBiography() != null) profile.setBiography(request.getBiography());
        if (request.getNationality() != null) profile.setNationality(request.getNationality());
        if (request.getPreferredLanguage() != null) profile.setPreferredLanguage(request.getPreferredLanguage());
        if (request.getTimezone() != null) profile.setTimezone(request.getTimezone());
        
        if (request.getWebsite() != null) profile.setWebsite(request.getWebsite());
        if (request.getLinkedin() != null) profile.setLinkedin(request.getLinkedin());
        if (request.getGithub() != null) profile.setGithub(request.getGithub());
        if (request.getGoogleScholar() != null) profile.setGoogleScholar(request.getGoogleScholar());
        if (request.getResearchgate() != null) profile.setResearchgate(request.getResearchgate());
        if (request.getOrcid() != null) profile.setOrcid(request.getOrcid());

        profile = profileRepository.save(profile);

        // Manage Addresses
        if (request.getAddresses() != null) {
            List<UserAddress> existingAddresses = addressRepository.findByUserId(user.getId());
            addressRepository.deleteAll(existingAddresses);

            List<UserAddress> newAddresses = request.getAddresses().stream().map(dto -> {
                UserAddress address = new UserAddress();
                address.setUser(user);
                address.setAddressType(dto.getAddressType());
                address.setAddressLine1(dto.getAddressLine1());
                address.setAddressLine2(dto.getAddressLine2());
                address.setCity(dto.getCity());
                address.setDistrict(dto.getDistrict());
                address.setProvince(dto.getProvince());
                address.setPostalCode(dto.getPostalCode());
                address.setCountry(dto.getCountry());
                address.setPrimary(dto.getIsPrimary() != null && dto.getIsPrimary());
                return address;
            }).collect(Collectors.toList());
            addressRepository.saveAll(newAddresses);
        }

        // Manage Social Links
        if (request.getCustomSocialLinks() != null) {
            List<SocialLink> existingLinks = socialLinkRepository.findByUserId(user.getId());
            socialLinkRepository.deleteAll(existingLinks);

            List<SocialLink> newLinks = request.getCustomSocialLinks().stream().map(dto -> {
                SocialLink link = new SocialLink();
                link.setUser(user);
                link.setPlatform(dto.getPlatform());
                link.setProfileUrl(dto.getProfileUrl());
                return link;
            }).collect(Collectors.toList());
            socialLinkRepository.saveAll(newLinks);
        }

        log.info("Profile updated for user: {}", email);
        return getProfile(email);
    }

    @Override
    @Transactional
    public void updateProfilePhoto(String email, UpdatePhotoRequest request) {
        User user = getUserByEmail(email);
        user.setProfileImageMediaId(request.getMediaId());
        userRepository.save(user);

        profileRepository.findByUserId(user.getId()).ifPresent(p -> {
            p.setProfileImageMediaId(request.getMediaId());
            profileRepository.save(p);
        });
        log.info("Profile photo updated for user: {}", email);
    }

    @Override
    @Transactional
    public void deleteProfilePhoto(String email) {
        User user = getUserByEmail(email);
        user.setProfileImageMediaId(null);
        userRepository.save(user);

        profileRepository.findByUserId(user.getId()).ifPresent(p -> {
            p.setProfileImageMediaId(null);
            profileRepository.save(p);
        });
        log.info("Profile photo deleted for user: {}", email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getActiveSessions(String email) {
        User user = getUserByEmail(email);
        List<UserSession> sessions = sessionRepository.findByUserId(user.getId());
        return sessions.stream()
                .filter(s -> s.isActive() && s.getExpiresAt() != null && s.getExpiresAt().isAfter(LocalDateTime.now()))
                .map(s -> SessionResponse.builder()
                        .id(s.getId())
                        .token("******") // Mask the token
                        .ipAddress(s.getIpAddress())
                        .userAgent(s.getUserAgent())
                        .expiresAt(s.getExpiresAt())
                        .createdAt(s.getCreatedAt())
                        .isRevoked(!s.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void terminateSession(String email, UUID sessionId) {
        User user = getUserByEmail(email);
        UserSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id " + sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only terminate your own sessions");
        }

        session.setActive(false);
        sessionRepository.save(session);
        log.info("Session {} terminated for user {}", sessionId, email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoginHistoryResponse> getLoginHistory(String email) {
        User user = getUserByEmail(email);
        List<LoginHistory> history = loginHistoryRepository.findByUserId(user.getId());
        return history.stream()
                .map(h -> LoginHistoryResponse.builder()
                        .id(h.getId())
                        .ipAddress(h.getIpAddress())
                        .userAgent(h.getUserAgent())
                        .status(h.isLoginSuccess() ? "SUCCESS" : "FAILED")
                        .failureReason(h.getFailureReason())
                        .loginAt(h.getLoginTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void softDeleteAccount(String email) {
        User user = getUserByEmail(email);
        user.setActive(false);
        userRepository.save(user);
        // Note: The schema has deleted_at column in uth.users, but not in User entity yet, or maybe it's not mapped.
        // We will just set isActive to false which acts as a soft delete.
        log.info("Account soft deleted for user: {}", email);
    }

    @Override
    @Transactional(readOnly = true)
    public PreferenceResponse getPreferences(String email) {
        User user = getUserByEmail(email);
        UserPreference preference = preferenceRepository.findByUserId(user.getId())
                .orElse(new UserPreference());
        
        return PreferenceResponse.builder()
                .theme(preference.getTheme())
                .language(preference.getLanguage())
                .emailNotifications(preference.isEmailNotifications())
                .smsNotifications(preference.isSmsNotifications())
                .pushNotifications(preference.isPushNotifications())
                .build();
    }

    @Override
    @Transactional
    public PreferenceResponse updatePreferences(String email, UpdatePreferencesRequest request) {
        User user = getUserByEmail(email);
        UserPreference preference = preferenceRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    UserPreference pref = new UserPreference();
                    pref.setUser(user);
                    return pref;
                });

        if (request.getTheme() != null) preference.setTheme(request.getTheme());
        if (request.getLanguage() != null) preference.setLanguage(request.getLanguage());
        if (request.getEmailNotifications() != null) preference.setEmailNotifications(request.getEmailNotifications());
        if (request.getSmsNotifications() != null) preference.setSmsNotifications(request.getSmsNotifications());
        if (request.getPushNotifications() != null) preference.setPushNotifications(request.getPushNotifications());

        preference = preferenceRepository.save(preference);
        log.info("Preferences updated for user: {}", email);

        return PreferenceResponse.builder()
                .theme(preference.getTheme())
                .language(preference.getLanguage())
                .emailNotifications(preference.isEmailNotifications())
                .smsNotifications(preference.isSmsNotifications())
                .pushNotifications(preference.isPushNotifications())
                .build();
    }

    @Override
    @Transactional
    public void updatePrivacySettings(String email, UpdatePrivacyRequest request) {
        // No underlying DB column exists yet. This is a placeholder as requested.
        log.info("Privacy settings update requested for user: {}", email);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountInformation(String email) {
        User user = getUserByEmail(email);
        return AccountResponse.builder()
                .email(user.getEmail())
                .emailVerified(user.isEmailVerified())
                .accountStatus(user.getAccountStatus().name())
                .isActive(user.isActive())
                .lastLoginAt(user.getLastLoginAt())
                .authProvider(user.getAuthProvider().name())
                .employeeNumber(user.getEmployeeNumber())
                .studentNumber(user.getStudentNumber())
                .build();
    }
}
