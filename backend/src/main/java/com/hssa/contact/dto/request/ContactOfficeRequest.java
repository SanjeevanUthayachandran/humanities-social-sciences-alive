package com.hssa.contact.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContactOfficeRequest {
    @NotBlank(message = "Office name is required")
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