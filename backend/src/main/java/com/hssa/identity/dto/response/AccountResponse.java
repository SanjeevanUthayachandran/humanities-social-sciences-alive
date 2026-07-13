package com.hssa.identity.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AccountResponse {
    private String email;
    private Boolean emailVerified;
    private String accountStatus;
    private Boolean isActive;
    private LocalDateTime lastLoginAt;
    private String authProvider;
    private String employeeNumber;
    private String studentNumber;
}
