package com.hssa.user.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserDetailsResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private String phoneNumber;
    private boolean isActive;
    private boolean emailVerified;
    private String accountStatus;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private List<RoleResponse> roles;
    private List<PermissionResponse> permissions;
}
