package com.hssa.identity.service;

import com.hssa.identity.dto.request.UpdatePhotoRequest;
import com.hssa.identity.dto.request.UpdatePreferencesRequest;
import com.hssa.identity.dto.request.UpdatePrivacyRequest;
import com.hssa.identity.dto.request.UpdateProfileRequest;
import com.hssa.identity.dto.response.AccountResponse;
import com.hssa.identity.dto.response.LoginHistoryResponse;
import com.hssa.identity.dto.response.PreferenceResponse;
import com.hssa.identity.dto.response.ProfileResponse;
import com.hssa.identity.dto.response.SessionResponse;

import java.util.List;
import java.util.UUID;

public interface ProfileService {
    ProfileResponse getProfile(String email);
    ProfileResponse updateProfile(String email, UpdateProfileRequest request);
    void updateProfilePhoto(String email, UpdatePhotoRequest request);
    void deleteProfilePhoto(String email);
    List<SessionResponse> getActiveSessions(String email);
    void terminateSession(String email, UUID sessionId);
    List<LoginHistoryResponse> getLoginHistory(String email);
    void softDeleteAccount(String email);
    PreferenceResponse getPreferences(String email);
    PreferenceResponse updatePreferences(String email, UpdatePreferencesRequest request);
    void updatePrivacySettings(String email, UpdatePrivacyRequest request);
    AccountResponse getAccountInformation(String email);
}
