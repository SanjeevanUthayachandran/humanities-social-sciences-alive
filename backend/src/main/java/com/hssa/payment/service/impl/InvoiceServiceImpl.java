package com.hssa.payment.service.impl;

import com.hssa.exception.ResourceNotFoundException;
import com.hssa.payment.dto.response.InvoiceItemResponse;
import com.hssa.payment.dto.response.InvoiceResponse;
import com.hssa.payment.dto.response.ReceiptResponse;
import com.hssa.payment.entity.Invoice;
import com.hssa.payment.entity.InvoiceItem;
import com.hssa.payment.repository.InvoiceItemRepository;
import com.hssa.payment.repository.InvoiceRepository;
import com.hssa.payment.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceByPaymentId(UUID paymentId) {
        Invoice invoice = invoiceRepository.findByPaymentIdAndDeletedAtIsNull(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for payment: " + paymentId));

        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceIdAndDeletedAtIsNull(invoice.getId());

        return InvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceNumber(invoice.getInvoiceNumber())
                .userId(invoice.getUser().getId())
                .studentName(invoice.getUser().getFirstName() + " " + invoice.getUser().getLastName())
                .status(invoice.getStatus().name())
                .totalAmount(invoice.getTotalAmount())
                .dueDate(invoice.getDueDate())
                .createdAt(invoice.getCreatedAt())
                .items(items.stream().map(this::mapToItemResponse).collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReceiptResponse getReceiptByPaymentId(UUID paymentId) {
        Invoice invoice = invoiceRepository.findByPaymentIdAndDeletedAtIsNull(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for payment: " + paymentId));

        if (!"PAID".equals(invoice.getStatus().name())) {
            throw new IllegalStateException("Cannot generate receipt for unpaid invoice");
        }

        return ReceiptResponse.builder()
                .paymentId(paymentId)
                .receiptNumber(invoice.getInvoiceNumber())
                .studentName(invoice.getUser().getFirstName() + " " + invoice.getUser().getLastName())
                .studentEmail(invoice.getUser().getEmail())
                .amountPaid(invoice.getTotalAmount())
                .paymentMethod(invoice.getPayment() != null ? invoice.getPayment().getPaymentMethod().name() : "N/A")
                .paymentDate(invoice.getPayment() != null ? invoice.getPayment().getUpdatedAt() : invoice.getUpdatedAt())
                .transactionReference(invoice.getPayment() != null ? invoice.getPayment().getId().toString() : "N/A")
                .build();
    }

    private InvoiceItemResponse mapToItemResponse(InvoiceItem item) {
        return InvoiceItemResponse.builder()
                .id(item.getId())
                .itemType(item.getItemType())
                .itemId(item.getItemId())
                .description(item.getDescription())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}
