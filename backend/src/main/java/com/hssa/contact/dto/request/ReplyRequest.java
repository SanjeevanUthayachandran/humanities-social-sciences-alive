package com.hssa.contact.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReplyRequest {
    @NotBlank(message = "Reply message is required")
    private String replyMessage;
}