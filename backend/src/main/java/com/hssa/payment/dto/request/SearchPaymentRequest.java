package com.hssa.payment.dto.request;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SearchPaymentRequest {
    private UUID userId;
    private UUID enrollmentId;
    private String transactionId;
    private String method;
    private String status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
}
