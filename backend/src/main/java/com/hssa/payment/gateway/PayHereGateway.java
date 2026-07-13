package com.hssa.payment.gateway;

import com.hssa.payment.dto.request.WebhookRequest;
import com.hssa.payment.entity.Payment;
import com.hssa.payment.entity.PaymentTransaction;
import com.hssa.payment.enums.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Component
public class PayHereGateway implements PaymentGateway {

    @Value("${payhere.merchant.id:1234567}")
    private String merchantId;

    @Value("${payhere.secret:test-secret}")
    private String merchantSecret;

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.PAYHERE;
    }

    @Override
    public String initiatePayment(Payment payment, PaymentTransaction transaction) {
        // Return a form or URL for PayHere checkout, or return MD5 hash if frontend handles form
        String hash = generateHash(payment.getId().toString(), payment.getAmount().toString(), payment.getCurrency());
        return hash;
    }

    @Override
    public boolean verifyPayment(PaymentTransaction transaction, String externalTransactionId) {
        // Verify payment through API (Optional for PayHere as webhook is preferred)
        return true; 
    }

    @Override
    public boolean processWebhook(PaymentTransaction transaction, Object webhookPayload) {
        if (webhookPayload instanceof WebhookRequest) {
            WebhookRequest request = (WebhookRequest) webhookPayload;
            String generatedHash = generateHash(request.getOrder_id(), request.getPayhere_amount(), request.getPayhere_currency(), request.getStatus_code());
            
            if (generatedHash.equalsIgnoreCase(request.getMd5sig())) {
                transaction.setGatewayTransactionId(request.getPayment_id());
                // 2 is success in PayHere
                return "2".equals(request.getStatus_code());
            }
        }
        return false;
    }

    @Override
    public boolean refundPayment(PaymentTransaction transaction) {
        // PayHere Refund API integration goes here
        return true; 
    }

    private String generateHash(String orderId, String amount, String currency) {
        return generateHash(orderId, amount, currency, null);
    }

    private String generateHash(String orderId, String amount, String currency, String statusCode) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] secretHashBytes = md.digest(merchantSecret.getBytes());
            StringBuilder secretHash = new StringBuilder();
            for (byte b : secretHashBytes) {
                secretHash.append(String.format("%02X", b));
            }
            
            String md5HashString = merchantId + orderId + amount + currency + (statusCode != null ? statusCode : "") + secretHash.toString();
            byte[] finalHashBytes = md.digest(md5HashString.getBytes());
            StringBuilder finalHash = new StringBuilder();
            for (byte b : finalHashBytes) {
                finalHash.append(String.format("%02X", b));
            }
            return finalHash.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            log.error("Error generating PayHere Hash", e);
            return "";
        }
    }
}
