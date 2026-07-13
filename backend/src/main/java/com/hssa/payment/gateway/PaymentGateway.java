package com.hssa.payment.gateway;

import com.hssa.payment.entity.Payment;
import com.hssa.payment.entity.PaymentTransaction;
import com.hssa.payment.enums.PaymentMethod;

public interface PaymentGateway {
    PaymentMethod getSupportedMethod();
    String initiatePayment(Payment payment, PaymentTransaction transaction);
    boolean verifyPayment(PaymentTransaction transaction, String externalTransactionId);
    boolean processWebhook(PaymentTransaction transaction, Object webhookPayload);
    boolean refundPayment(PaymentTransaction transaction);
}
