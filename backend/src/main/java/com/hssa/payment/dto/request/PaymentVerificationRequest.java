package com.hssa.payment.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class PaymentVerificationRequest {
    @NotBlank(message = "Gateway Transaction ID is required")
    private String gatewayTransactionId;
}
