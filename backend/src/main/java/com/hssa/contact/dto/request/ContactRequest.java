package com.hssa.contact.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private String phoneNumber;
    @NotBlank(message = "Subject is required")
    private String subject;
    @NotBlank(message = "Inquiry type is required")
    private String inquiryType;
    @NotBlank(message = "Message is required")
    private String message;
}