package com.hssa.identity.dto.request;

import lombok.Data;

@Data
public class UpdatePreferencesRequest {
    private String theme;
    private String language;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
}
