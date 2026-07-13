package com.hssa.identity.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class UpdateProfileRequest {
    @Size(max = 100)
    private String firstName;
    
    @Size(max = 100)
    private String middleName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 200)
    private String displayName;

    @Size(max = 30)
    private String phoneNumber;

    @Size(max = 100)
    private String title;

    private String biography;

    @Size(max = 100)
    private String nationality;

    @Size(max = 50)
    private String preferredLanguage;

    @Size(max = 100)
    private String timezone;

    private String website;
    private String linkedin;
    private String github;
    private String googleScholar;
    private String researchgate;

    @Size(max = 100)
    private String orcid;

    private List<UserAddressDto> addresses;
    private List<SocialLinkDto> customSocialLinks;
}
