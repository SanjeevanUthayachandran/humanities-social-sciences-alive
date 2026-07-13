package com.hssa.user.controller;

import com.hssa.common.response.ApiResponse;
import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.user.dto.request.*;
import com.hssa.user.dto.response.*;
import com.hssa.user.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin - User Management", description = "Endpoints for administrator user management")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of all users")
    public ResponseEntity<ApiResponse<PaginationResponse<UserSummaryResponse>>> getAllUsers(Pageable pageable) {
        Page<UserSummaryResponse> page = adminUserService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Users retrieved successfully"));
    }

    @PostMapping("/search")
    @Operation(summary = "Search users", description = "Search and filter users dynamically")
    public ResponseEntity<ApiResponse<PaginationResponse<UserSummaryResponse>>> searchUsers(
            @RequestBody SearchUserRequest request, Pageable pageable) {
        Page<UserSummaryResponse> page = adminUserService.searchUsers(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Users searched successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user details", description = "Retrieve detailed information of a specific user")
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getUserDetails(@PathVariable UUID id) {
        UserDetailsResponse response = adminUserService.getUserDetails(id);
        return ResponseEntity.ok(ApiResponse.success(response, "User details retrieved successfully"));
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate user", description = "Activate a deactivated user")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable UUID id) {
        adminUserService.activateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User activated successfully"));
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate user", description = "Deactivate an active user")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable UUID id) {
        adminUserService.deactivateUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User deactivated successfully"));
    }

    @PatchMapping("/{id}/lock")
    @Operation(summary = "Lock user", description = "Lock a user account")
    public ResponseEntity<ApiResponse<Void>> lockUser(@PathVariable UUID id) {
        adminUserService.lockUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User locked successfully"));
    }

    @PatchMapping("/{id}/unlock")
    @Operation(summary = "Unlock user", description = "Unlock a locked user account")
    public ResponseEntity<ApiResponse<Void>> unlockUser(@PathVariable UUID id) {
        adminUserService.unlockUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User unlocked successfully"));
    }

    @PatchMapping("/{id}/roles")
    @Operation(summary = "Assign roles", description = "Assign multiple roles to a user")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRoleRequest request) {
        adminUserService.assignRoles(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Roles assigned successfully"));
    }

    @PatchMapping("/{id}/permissions")
    @Operation(summary = "Assign permissions", description = "Assign specific permissions to a user (Not Supported)")
    public ResponseEntity<ApiResponse<Void>> assignPermissions(
            @PathVariable UUID id,
            @Valid @RequestBody AssignPermissionRequest request) {
        adminUserService.assignPermissions(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Permissions assigned successfully"));
    }

    @PatchMapping("/{id}/reset-password")
    @Operation(summary = "Reset password", description = "Reset password for a user account")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @PathVariable UUID id,
            @Valid @RequestBody ResetPasswordRequest request) {
        adminUserService.resetUserPassword(id, request);
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete user", description = "Soft delete a user account")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        adminUserService.softDeleteUser(id);
        return ResponseEntity.ok(ApiResponse.success(null, "User soft deleted successfully"));
    }

    @PostMapping("/bulk")
    @Operation(summary = "Bulk operations", description = "Perform bulk operations on multiple users")
    public ResponseEntity<ApiResponse<Void>> bulkOperations(
            @Valid @RequestBody BulkOperationRequest request,
            @RequestParam String action) {
        
        switch (action.toLowerCase()) {
            case "activate":
                adminUserService.bulkActivate(request);
                break;
            case "deactivate":
                adminUserService.bulkDeactivate(request);
                break;
            case "delete":
                adminUserService.bulkDelete(request);
                break;
            case "assign_roles":
                adminUserService.bulkRoleAssignment(request);
                break;
            default:
                throw new IllegalArgumentException("Invalid bulk action: " + action);
        }
        
        return ResponseEntity.ok(ApiResponse.success(null, "Bulk operation '" + action + "' executed successfully"));
    }

    @GetMapping("/export")
    @Operation(summary = "Export users", description = "Export filtered users to a CSV file")
    public ResponseEntity<byte[]> exportUsers(@ModelAttribute SearchUserRequest request) {
        byte[] csvData = adminUserService.exportUserList(request);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users_export.csv");
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}
