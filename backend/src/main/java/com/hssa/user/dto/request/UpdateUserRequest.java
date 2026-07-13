package com.hssa.user.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;
    private String phoneNumber;
}
