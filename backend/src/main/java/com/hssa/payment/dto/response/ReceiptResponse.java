package com.hssa.payment.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReceiptResponse {
    private UUID paymentId;
    private String receiptNumber; // Maps to invoiceNumber typically
    private String studentName;
    private String studentEmail;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private LocalDateTime paymentDate;
    private String transactionReference;
}
