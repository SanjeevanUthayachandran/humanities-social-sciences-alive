package com.hssa.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class AssignPermissionRequest {
    @NotEmpty
    private List<String> permissionCodes;
}
