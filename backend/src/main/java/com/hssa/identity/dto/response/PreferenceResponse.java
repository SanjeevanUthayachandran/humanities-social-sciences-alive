package com.hssa.identity.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PreferenceResponse {
    private String theme;
    private String language;
    private Boolean emailNotifications;
    private Boolean smsNotifications;
    private Boolean pushNotifications;
}
