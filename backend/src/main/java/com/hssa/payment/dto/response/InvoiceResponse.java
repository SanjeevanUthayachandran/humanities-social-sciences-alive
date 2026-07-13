package com.hssa.payment.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Data
@Builder
public class InvoiceResponse {
    private UUID id;
    private String invoiceNumber;
    private UUID userId;
    private String studentName;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private List<InvoiceItemResponse> items;
}
