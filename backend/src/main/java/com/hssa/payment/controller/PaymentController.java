package com.hssa.payment.controller;

import com.hssa.payment.dto.request.PaymentRequest;
import com.hssa.payment.dto.request.PaymentVerificationRequest;
import com.hssa.payment.dto.request.SearchPaymentRequest;
import com.hssa.payment.dto.request.WebhookRequest;
import com.hssa.payment.dto.response.InvoiceResponse;
import com.hssa.payment.dto.response.PaymentResponse;
import com.hssa.payment.dto.response.PaymentSummaryResponse;
import com.hssa.payment.dto.response.ReceiptResponse;
import com.hssa.payment.service.InvoiceService;
import com.hssa.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final InvoiceService invoiceService;

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.initiatePayment(request));
    }

    @PostMapping("/verify/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> verifyPayment(
            @PathVariable("id") UUID paymentId,
            @Valid @RequestBody PaymentVerificationRequest request) {
        paymentService.verifyPayment(paymentId, request);
        return ResponseEntity.ok().build();
    }

    // Public endpoint for webhook
    @PostMapping(value = "/webhook", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<Void> processWebhook(@ModelAttribute WebhookRequest request) {
        paymentService.processWebhook(request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> cancelPayment(@PathVariable UUID id) {
        paymentService.cancelPayment(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> refundPayment(@PathVariable UUID id) {
        paymentService.refundPayment(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable UUID id) {
        return ResponseEntity.ok(paymentService.getPaymentDetails(id));
    }

    @GetMapping("/history/{userId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #userId")
    public ResponseEntity<Page<PaymentSummaryResponse>> getPaymentHistory(
            @PathVariable UUID userId,
            Pageable pageable) {
        return ResponseEntity.ok(paymentService.getPaymentHistory(userId, pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentSummaryResponse>> searchPayments(
            @ModelAttribute SearchPaymentRequest request,
            Pageable pageable) {
        return ResponseEntity.ok(paymentService.searchPayments(request, pageable));
    }

    @GetMapping("/invoice/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<InvoiceResponse> getInvoice(@PathVariable("id") UUID paymentId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByPaymentId(paymentId));
    }

    @GetMapping("/receipt/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ReceiptResponse> getReceipt(@PathVariable("id") UUID paymentId) {
        return ResponseEntity.ok(invoiceService.getReceiptByPaymentId(paymentId));
    }
}
