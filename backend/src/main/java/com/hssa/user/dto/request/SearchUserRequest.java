package com.hssa.user.dto.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SearchUserRequest {
    private String search; // general keyword
    private String email;
    private String role;
    private String status;
    private String department;
    private String institution;
    private String country;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private String accountStatus;
}
