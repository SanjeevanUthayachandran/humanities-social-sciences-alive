package com.hssa.contact.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ContactResponse {
    private UUID id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String subject;
    private String inquiryType;
    private String message;
    private String status;
    private String priority;
    private String assignedToName;
    private String resolvedByName;
    private String replyMessage;
    private LocalDateTime createdAt;
}