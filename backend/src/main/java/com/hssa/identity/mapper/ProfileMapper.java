package com.hssa.identity.mapper;

import com.hssa.auth.entity.User;
import com.hssa.identity.dto.request.SocialLinkDto;
import com.hssa.identity.dto.request.UserAddressDto;
import com.hssa.identity.dto.response.ProfileResponse;
import com.hssa.identity.entity.Profile;
import com.hssa.identity.entity.SocialLink;
import com.hssa.identity.entity.UserAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProfileMapper {

    public ProfileResponse toProfileResponse(User user, Profile profile, List<UserAddress> addresses, List<SocialLink> socialLinks) {
        ProfileResponse.ProfileResponseBuilder builder = ProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .displayName(user.getDisplayName())
                .gender(user.getGender() != null ? user.getGender().name() : null)
                .dateOfBirth(user.getDateOfBirth())
                .profileImageMediaId(user.getProfileImageMediaId())
                .phoneNumber(user.getPhoneNumber())
                
                // Fields from User which might be academic details
                .qualification(user.getQualification())
                .specialization(user.getSpecialization())
                .designation(user.getDesignation())
                .department(user.getDepartment());

        if (profile != null) {
            builder.title(profile.getTitle())
                   .biography(profile.getBiography())
                   .nationality(profile.getNationality())
                   .preferredLanguage(profile.getPreferredLanguage())
                   .timezone(profile.getTimezone())
                   .website(profile.getWebsite())
                   .linkedin(profile.getLinkedin())
                   .github(profile.getGithub())
                   .googleScholar(profile.getGoogleScholar())
                   .researchgate(profile.getResearchgate())
                   .orcid(profile.getOrcid());
        }

        if (addresses != null) {
            builder.addresses(addresses.stream().map(this::toAddressDto).collect(Collectors.toList()));
        }

        if (socialLinks != null) {
            builder.customSocialLinks(socialLinks.stream().map(this::toSocialLinkDto).collect(Collectors.toList()));
        }

        return builder.build();
    }

    public UserAddressDto toAddressDto(UserAddress address) {
        UserAddressDto dto = new UserAddressDto();
        dto.setAddressType(address.getAddressType());
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setDistrict(address.getDistrict());
        dto.setProvince(address.getProvince());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setIsPrimary(address.isPrimary());
        return dto;
    }

    public SocialLinkDto toSocialLinkDto(SocialLink socialLink) {
        SocialLinkDto dto = new SocialLinkDto();
        dto.setPlatform(socialLink.getPlatform());
        dto.setProfileUrl(socialLink.getProfileUrl());
        return dto;
    }
}
