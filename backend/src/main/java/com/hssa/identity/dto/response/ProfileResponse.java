package com.hssa.identity.dto.response;

import com.hssa.identity.dto.request.SocialLinkDto;
import com.hssa.identity.dto.request.UserAddressDto;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProfileResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private String gender;
    private LocalDate dateOfBirth;
    private UUID profileImageMediaId;
    private String phoneNumber;
    
    // Extended profile details
    private String title;
    private String biography;
    private String nationality;
    private String preferredLanguage;
    private String timezone;
    private String website;
    private String linkedin;
    private String github;
    private String googleScholar;
    private String researchgate;
    private String orcid;
    
    // Academic details
    private String qualification;
    private String specialization;
    private String designation;
    private String department;

    private List<UserAddressDto> addresses;
    private List<SocialLinkDto> customSocialLinks;
}
