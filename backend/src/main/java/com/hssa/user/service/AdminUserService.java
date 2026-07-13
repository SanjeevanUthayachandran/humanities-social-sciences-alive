package com.hssa.user.service;

import com.hssa.user.dto.request.*;
import com.hssa.user.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AdminUserService {

    // View Users
    Page<UserSummaryResponse> getAllUsers(Pageable pageable);
    
    // Search / Filter
    Page<UserSummaryResponse> searchUsers(SearchUserRequest request, Pageable pageable);
    
    // User Details
    UserDetailsResponse getUserDetails(UUID id);
    
    // Status Toggles
    void activateUser(UUID id);
    void deactivateUser(UUID id);
    void lockUser(UUID id);
    void unlockUser(UUID id);
    void suspendUser(UUID id);
    void restoreUser(UUID id);
    void softDeleteUser(UUID id);
    
    // Roles
    void assignRoles(UUID id, AssignRoleRequest request);
    void removeRoles(UUID id, AssignRoleRequest request);
    List<RoleResponse> getUserRoles(UUID id);
    
    // Permissions
    List<PermissionResponse> getUserPermissions(UUID id);
    void assignPermissions(UUID id, AssignPermissionRequest request);
    void removePermissions(UUID id, AssignPermissionRequest request);
    
    // Password
    void resetUserPassword(UUID id, ResetPasswordRequest request);
    
    // Sessions & Login
    void forceLogoutUser(UUID id);
    List<SessionResponse> getUserSessions(UUID id);
    List<LoginHistoryResponse> getUserLoginHistory(UUID id);
    
    // Export (Returns CSV content as byte array or stream)
    byte[] exportUserList(SearchUserRequest request);
    
    // Bulk Operations
    void bulkActivate(BulkOperationRequest request);
    void bulkDeactivate(BulkOperationRequest request);
    void bulkDelete(BulkOperationRequest request);
    void bulkRoleAssignment(BulkOperationRequest request);
}
