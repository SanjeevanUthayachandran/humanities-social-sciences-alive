package com.hssa.contact.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FAQResponse {
    private UUID id;
    private String question;
    private String answer;
    private Integer displayOrder;
    private Boolean isActive;
}