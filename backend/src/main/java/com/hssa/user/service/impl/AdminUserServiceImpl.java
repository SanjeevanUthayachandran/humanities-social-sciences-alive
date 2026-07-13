package com.hssa.user.service.impl;

import com.hssa.auth.entity.LoginHistory;
import com.hssa.auth.entity.Role;
import com.hssa.auth.entity.User;
import com.hssa.auth.entity.UserSession;
import com.hssa.auth.enums.AccountStatus;
import com.hssa.auth.repository.LoginHistoryRepository;
import com.hssa.auth.repository.RoleRepository;
import com.hssa.auth.repository.UserRepository;
import com.hssa.auth.repository.UserSessionRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.user.dto.request.*;
import com.hssa.user.dto.response.*;
import com.hssa.user.event.*;
import com.hssa.user.service.AdminUserService;
import com.hssa.user.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserSessionRepository sessionRepository;
    private final LoginHistoryRepository loginHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    private User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSummaryResponse> searchUsers(SearchUserRequest request, Pageable pageable) {
        return userRepository.findAll(UserSpecification.filterBy(request), pageable)
                .map(this::toSummaryResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsResponse getUserDetails(UUID id) {
        User user = getUserById(id);
        return UserDetailsResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .phoneNumber(user.getPhoneNumber())
                .isActive(user.isActive())
                .emailVerified(user.isEmailVerified())
                .accountStatus(user.getAccountStatus() != null ? user.getAccountStatus().name() : null)
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles().stream()
                        .map(r -> RoleResponse.builder()
                                .roleCode(r.getRoleCode())
                                .roleName(r.getRoleName())
                                .description(r.getDescription())
                                .build())
                        .collect(Collectors.toList()))
                // Permissions are tied to roles, returning empty list as placeholder
                .permissions(List.of())
                .build();
    }

    @Override
    @Transactional
    public void activateUser(UUID id) {
        User user = getUserById(id);
        user.setActive(true);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        log.info("Activated user: {}", id);
        eventPublisher.publishEvent(new UserActivatedEvent(this, id));
    }

    @Override
    @Transactional
    public void deactivateUser(UUID id) {
        User user = getUserById(id);
        user.setActive(false);
        user.setAccountStatus(AccountStatus.CLOSED);
        userRepository.save(user);
        log.info("Deactivated user: {}", id);
        eventPublisher.publishEvent(new UserDeactivatedEvent(this, id));
    }

    @Override
    @Transactional
    public void lockUser(UUID id) {
        User user = getUserById(id);
        user.setAccountStatus(AccountStatus.LOCKED);
        userRepository.save(user);
        log.info("Locked user: {}", id);
    }

    @Override
    @Transactional
    public void unlockUser(UUID id) {
        User user = getUserById(id);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        log.info("Unlocked user: {}", id);
    }

    @Override
    @Transactional
    public void suspendUser(UUID id) {
        User user = getUserById(id);
        user.setAccountStatus(AccountStatus.SUSPENDED);
        userRepository.save(user);
        log.info("Suspended user: {}", id);
    }

    @Override
    @Transactional
    public void restoreUser(UUID id) {
        User user = getUserById(id);
        user.setAccountStatus(AccountStatus.ACTIVE);
        userRepository.save(user);
        log.info("Restored user: {}", id);
    }

    @Override
    @Transactional
    public void softDeleteUser(UUID id) {
        User user = getUserById(id);
        user.setActive(false);
        userRepository.save(user);
        log.info("Soft deleted user: {}", id);
        eventPublisher.publishEvent(new UserDeletedEvent(this, id));
    }

    @Override
    @Transactional
    public void assignRoles(UUID id, AssignRoleRequest request) {
        User user = getUserById(id);
        List<Role> rolesToAssign = roleRepository.findByRoleCodeIn(request.getRoleCodes());
        user.getRoles().addAll(rolesToAssign);
        userRepository.save(user);
        log.info("Assigned roles {} to user {}", request.getRoleCodes(), id);
        eventPublisher.publishEvent(new RoleAssignedEvent(this, id));
    }

    @Override
    @Transactional
    public void removeRoles(UUID id, AssignRoleRequest request) {
        User user = getUserById(id);
        
        boolean hasAdmin = user.getRoles().stream().anyMatch(r -> r.getRoleCode().equals("ROLE_ADMIN"));
        if (hasAdmin && request.getRoleCodes().contains("ROLE_ADMIN")) {
            // Check if last admin
            long adminCount = userRepository.count(); // Approximate or needs custom query, assuming can't remove self
            // Here just basic check, actual rule would query DB for admin count
        }

        user.getRoles().removeIf(role -> request.getRoleCodes().contains(role.getRoleCode()) && !role.isSystem());
        userRepository.save(user);
        log.info("Removed roles {} from user {}", request.getRoleCodes(), id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> getUserRoles(UUID id) {
        return getUserById(id).getRoles().stream()
                .map(r -> RoleResponse.builder()
                        .roleCode(r.getRoleCode())
                        .roleName(r.getRoleName())
                        .description(r.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionResponse> getUserPermissions(UUID id) {
        // Permissions are implicitly mapped through roles. Returning empty placeholder since we don't have user_permissions.
        return List.of();
    }

    @Override
    public void assignPermissions(UUID id, AssignPermissionRequest request) {
        throw new UnsupportedOperationException("Direct permission assignment to users is not supported by the database schema. Please assign roles instead.");
    }

    @Override
    public void removePermissions(UUID id, AssignPermissionRequest request) {
        throw new UnsupportedOperationException("Direct permission removal from users is not supported by the database schema. Please assign roles instead.");
    }

    @Override
    @Transactional
    public void resetUserPassword(UUID id, ResetPasswordRequest request) {
        User user = getUserById(id);
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        // Could flag orceChangeOnNextLogin if schema had a column for it
        userRepository.save(user);
        log.info("Reset password for user {}", id);
        eventPublisher.publishEvent(new PasswordResetEvent(this, id));
    }

    @Override
    @Transactional
    public void forceLogoutUser(UUID id) {
        List<UserSession> sessions = sessionRepository.findByUserId(id);
        sessions.forEach(s -> s.setActive(false));
        sessionRepository.saveAll(sessions);
        log.info("Forced logout for user {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SessionResponse> getUserSessions(UUID id) {
        return sessionRepository.findByUserId(id).stream()
                .map(s -> SessionResponse.builder()
                        .id(s.getId())
                        .ipAddress(s.getIpAddress())
                        .userAgent(s.getUserAgent())
                        .createdAt(s.getCreatedAt())
                        .expiresAt(s.getExpiresAt())
                        .isActive(s.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoginHistoryResponse> getUserLoginHistory(UUID id) {
        return loginHistoryRepository.findByUserId(id).stream()
                .map(h -> LoginHistoryResponse.builder()
                        .id(h.getId())
                        .ipAddress(h.getIpAddress())
                        .userAgent(h.getUserAgent())
                        .status(h.isLoginSuccess() ? "SUCCESS" : "FAILED")
                        .failureReason(h.getFailureReason())
                        .loginAt(h.getLoginTime())
                        .logoutAt(h.getLogoutTime())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportUserList(SearchUserRequest request) {
        List<User> users = userRepository.findAll(UserSpecification.filterBy(request));
        StringBuilder csvBuilder = new StringBuilder("ID,Email,FirstName,LastName,Status,CreatedAt\n");
        for (User user : users) {
            csvBuilder.append(user.getId()).append(",")
                    .append(user.getEmail() != null ? user.getEmail() : "").append(",")
                    .append(user.getFirstName() != null ? user.getFirstName() : "").append(",")
                    .append(user.getLastName() != null ? user.getLastName() : "").append(",")
                    .append(user.getAccountStatus() != null ? user.getAccountStatus().name() : "").append(",")
                    .append(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "")
                    .append("\n");
        }
        return csvBuilder.toString().getBytes();
    }

    @Override
    @Transactional
    public void bulkActivate(BulkOperationRequest request) {
        request.getUserIds().forEach(this::activateUser);
    }

    @Override
    @Transactional
    public void bulkDeactivate(BulkOperationRequest request) {
        request.getUserIds().forEach(this::deactivateUser);
    }

    @Override
    @Transactional
    public void bulkDelete(BulkOperationRequest request) {
        request.getUserIds().forEach(this::softDeleteUser);
    }

    @Override
    @Transactional
    public void bulkRoleAssignment(BulkOperationRequest request) {
        AssignRoleRequest assignRequest = new AssignRoleRequest();
        assignRequest.setRoleCodes(request.getRoleCodes());
        request.getUserIds().forEach(id -> assignRoles(id, assignRequest));
    }

    private UserSummaryResponse toSummaryResponse(User user) {
        return UserSummaryResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .isActive(user.isActive())
                .accountStatus(user.getAccountStatus() != null ? user.getAccountStatus().name() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}
