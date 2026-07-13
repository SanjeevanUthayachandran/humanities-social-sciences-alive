package com.hssa.payment.service;

import com.hssa.payment.dto.response.InvoiceResponse;
import com.hssa.payment.dto.response.ReceiptResponse;

import java.util.UUID;

public interface InvoiceService {
    InvoiceResponse getInvoiceByPaymentId(UUID paymentId);
    ReceiptResponse getReceiptByPaymentId(UUID paymentId);
}
