package com.hssa.contact.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ContactOfficeResponse {
    private UUID id;
    private String officeName;
    private String address;
    private String phone;
    private String email;
    private String openingHours;
    private String googleMapUrl;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isHeadOffice;
    private Boolean isActive;
}