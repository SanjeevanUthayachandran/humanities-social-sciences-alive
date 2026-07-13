package com.hssa.payment.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class InvoiceItemResponse {
    private UUID id;
    private String itemType;
    private UUID itemId;
    private String description;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
}
