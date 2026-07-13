package com.hssa.payment.service;

import com.hssa.payment.dto.request.PaymentRequest;
import com.hssa.payment.dto.request.PaymentVerificationRequest;
import com.hssa.payment.dto.request.SearchPaymentRequest;
import com.hssa.payment.dto.request.WebhookRequest;
import com.hssa.payment.dto.response.PaymentResponse;
import com.hssa.payment.dto.response.PaymentSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse initiatePayment(PaymentRequest request);
    void verifyPayment(UUID paymentId, PaymentVerificationRequest request);
    void processWebhook(WebhookRequest request);
    void cancelPayment(UUID paymentId);
    void refundPayment(UUID paymentId);
    PaymentResponse getPaymentDetails(UUID paymentId);
    Page<PaymentSummaryResponse> searchPayments(SearchPaymentRequest request, Pageable pageable);
    Page<PaymentSummaryResponse> getPaymentHistory(UUID userId, Pageable pageable);
}
