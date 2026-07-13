package com.hssa.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private String permissionCode;
    private String permissionName;
}
