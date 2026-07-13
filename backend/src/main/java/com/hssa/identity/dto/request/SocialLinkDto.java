package com.hssa.identity.dto.request;

import lombok.Data;

@Data
public class SocialLinkDto {
    private String platform;
    private String profileUrl;
}
