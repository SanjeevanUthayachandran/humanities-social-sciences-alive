package com.hssa.user.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BulkOperationRequest {
    @NotEmpty
    private List<UUID> userIds;
    private List<String> roleCodes; // For bulk role assignment
}
