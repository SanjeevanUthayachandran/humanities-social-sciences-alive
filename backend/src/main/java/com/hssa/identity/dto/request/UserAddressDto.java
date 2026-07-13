package com.hssa.identity.dto.request;

import lombok.Data;

@Data
public class UserAddressDto {
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String district;
    private String province;
    private String postalCode;
    private String country;
    private Boolean isPrimary;
}
