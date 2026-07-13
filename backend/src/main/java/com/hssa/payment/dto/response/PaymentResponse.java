package com.hssa.payment.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private UUID id;
    private UUID userId;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String status;
    private String paymentUrl;
    private String hash; // specific to payhere
}
