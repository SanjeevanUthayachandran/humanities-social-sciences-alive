package com.hssa.identity.dto.request;

import lombok.Data;

@Data
public class UpdatePrivacyRequest {
    // There are no explicit privacy columns in the DB yet, but we will add a placeholder
    // for future use as requested by the requirements.
    private Boolean profileVisibleToPublic;
}
