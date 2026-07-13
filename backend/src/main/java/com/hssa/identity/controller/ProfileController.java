package com.hssa.identity.controller;

import com.hssa.common.response.ApiResponse;
import com.hssa.identity.dto.request.UpdatePhotoRequest;
import com.hssa.identity.dto.request.UpdatePreferencesRequest;
import com.hssa.identity.dto.request.UpdatePrivacyRequest;
import com.hssa.identity.dto.request.UpdateProfileRequest;
import com.hssa.identity.dto.response.AccountResponse;
import com.hssa.identity.dto.response.LoginHistoryResponse;
import com.hssa.identity.dto.response.PreferenceResponse;
import com.hssa.identity.dto.response.ProfileResponse;
import com.hssa.identity.dto.response.SessionResponse;
import com.hssa.identity.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "Identity & Profile Management API")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "View Profile", description = "Get the authenticated user's profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile(Authentication authentication) {
        ProfileResponse response = profileService.getProfile(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Profile retrieved successfully"));
    }

    @PutMapping
    @Operation(summary = "Update Profile", description = "Update the authenticated user's profile details")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {
        ProfileResponse response = profileService.updateProfile(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Profile updated successfully"));
    }

    @PatchMapping("/photo")
    @Operation(summary = "Change Profile Picture", description = "Update the authenticated user's profile picture")
    public ResponseEntity<ApiResponse<Void>> updateProfilePhoto(
            Authentication authentication,
            @Valid @RequestBody UpdatePhotoRequest request) {
        profileService.updateProfilePhoto(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Profile photo updated successfully"));
    }

    @DeleteMapping("/photo")
    @Operation(summary = "Delete Profile Picture", description = "Remove the authenticated user's profile picture")
    public ResponseEntity<ApiResponse<Void>> deleteProfilePhoto(Authentication authentication) {
        profileService.deleteProfilePhoto(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Profile photo deleted successfully"));
    }

    @GetMapping("/sessions")
    @Operation(summary = "View Active Sessions", description = "Get the authenticated user's active sessions")
    public ResponseEntity<ApiResponse<List<SessionResponse>>> getActiveSessions(Authentication authentication) {
        List<SessionResponse> response = profileService.getActiveSessions(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Sessions retrieved successfully"));
    }

    @DeleteMapping("/sessions/{id}")
    @Operation(summary = "Terminate Own Session", description = "Terminate a specific session")
    public ResponseEntity<ApiResponse<Void>> terminateSession(
            Authentication authentication,
            @PathVariable UUID id) {
        profileService.terminateSession(authentication.getName(), id);
        return ResponseEntity.ok(ApiResponse.success(null, "Session terminated successfully"));
    }

    @GetMapping("/login-history")
    @Operation(summary = "View Login History", description = "Get the authenticated user's login history")
    public ResponseEntity<ApiResponse<List<LoginHistoryResponse>>> getLoginHistory(Authentication authentication) {
        List<LoginHistoryResponse> response = profileService.getLoginHistory(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Login history retrieved successfully"));
    }

    @DeleteMapping
    @Operation(summary = "Delete Own Account", description = "Soft delete the authenticated user's account")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(Authentication authentication) {
        profileService.softDeleteAccount(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(null, "Account deleted successfully"));
    }

    @GetMapping("/preferences")
    @Operation(summary = "View Preferences", description = "Get the authenticated user's preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> getPreferences(Authentication authentication) {
        PreferenceResponse response = profileService.getPreferences(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Preferences retrieved successfully"));
    }

    @PutMapping("/preferences")
    @Operation(summary = "Update Preferences", description = "Update the authenticated user's preferences")
    public ResponseEntity<ApiResponse<PreferenceResponse>> updatePreferences(
            Authentication authentication,
            @Valid @RequestBody UpdatePreferencesRequest request) {
        PreferenceResponse response = profileService.updatePreferences(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(response, "Preferences updated successfully"));
    }

    @PutMapping("/privacy")
    @Operation(summary = "Update Privacy Settings", description = "Update the authenticated user's privacy settings")
    public ResponseEntity<ApiResponse<Void>> updatePrivacy(
            Authentication authentication,
            @Valid @RequestBody UpdatePrivacyRequest request) {
        profileService.updatePrivacySettings(authentication.getName(), request);
        return ResponseEntity.ok(ApiResponse.success(null, "Privacy settings updated successfully"));
    }

    @GetMapping("/account")
    @Operation(summary = "View Account Information", description = "Get the authenticated user's account information")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccountInformation(Authentication authentication) {
        AccountResponse response = profileService.getAccountInformation(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(response, "Account information retrieved successfully"));
    }
}
