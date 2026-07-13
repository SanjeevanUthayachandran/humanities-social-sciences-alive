package com.hssa.research.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ResearchMemberResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private boolean isPrimary;
}
