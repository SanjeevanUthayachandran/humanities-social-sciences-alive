package com.hssa.payment.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.enrollment.entity.Enrollment;
import com.hssa.enrollment.repository.EnrollmentRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.payment.dto.request.PaymentRequest;
import com.hssa.payment.dto.request.PaymentVerificationRequest;
import com.hssa.payment.dto.request.SearchPaymentRequest;
import com.hssa.payment.dto.request.WebhookRequest;
import com.hssa.payment.dto.response.PaymentResponse;
import com.hssa.payment.dto.response.PaymentSummaryResponse;
import com.hssa.payment.entity.Invoice;
import com.hssa.payment.entity.InvoiceItem;
import com.hssa.payment.entity.Payment;
import com.hssa.payment.entity.PaymentTransaction;
import com.hssa.payment.enums.InvoiceStatus;
import com.hssa.payment.enums.PaymentMethod;
import com.hssa.payment.enums.PaymentStatus;
import com.hssa.payment.event.*;
import com.hssa.payment.gateway.PaymentGateway;
import com.hssa.payment.gateway.PaymentGatewayFactory;
import com.hssa.payment.repository.InvoiceItemRepository;
import com.hssa.payment.repository.InvoiceRepository;
import com.hssa.payment.repository.PaymentRepository;
import com.hssa.payment.repository.PaymentTransactionRepository;
import com.hssa.payment.service.PaymentService;
import com.hssa.payment.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final PaymentGatewayFactory gatewayFactory;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        log.info("Initiating payment for enrollment: {}", request.getEnrollmentId());

        Enrollment enrollment = enrollmentRepository.findById(request.getEnrollmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        User user = enrollment.getUser();
        PaymentMethod method = PaymentMethod.valueOf(request.getMethod().toUpperCase());

        // Check if there's already a successful payment for this enrollment
        // We look up via invoice items
        boolean hasPaid = invoiceItemRepository.findByItemTypeAndItemIdAndDeletedAtIsNull("ENROLLMENT", enrollment.getId())
                .stream()
                .map(InvoiceItem::getInvoice)
                .filter(i -> i.getPayment() != null)
                .anyMatch(i -> i.getPayment().getStatus() == PaymentStatus.SUCCESS);
        
        if (hasPaid) {
            throw new BusinessException("Enrollment is already paid for");
        }

        Payment payment = Payment.builder()
                .user(user)
                .amount(request.getAmount())
                .paymentMethod(method)
                .status(PaymentStatus.PENDING)
                .build();
        payment = paymentRepository.save(payment);

        PaymentTransaction transaction = PaymentTransaction.builder()
                .payment(payment)
                .gatewayName(method.name())
                .status(PaymentStatus.PENDING)
                .build();
        transaction = paymentTransactionRepository.save(transaction);

        String invoiceNumber = "INV-" + System.currentTimeMillis();
        Invoice invoice = Invoice.builder()
                .user(user)
                .payment(payment)
                .invoiceNumber(invoiceNumber)
                .totalAmount(request.getAmount())
                .dueDate(LocalDateTime.now().plusDays(3))
                .status(InvoiceStatus.UNPAID)
                .build();
        invoice = invoiceRepository.save(invoice);

        InvoiceItem item = InvoiceItem.builder()
                .invoice(invoice)
                .itemType("ENROLLMENT")
                .itemId(enrollment.getId())
                .description("Enrollment for course: " + enrollment.getCourse().getTitle())
                .unitPrice(request.getAmount())
                .totalPrice(request.getAmount())
                .quantity(1)
                .build();
        invoiceItemRepository.save(item);

        PaymentGateway gateway = gatewayFactory.getGateway(method);
        String hashOrUrl = gateway.initiatePayment(payment, transaction);

        eventPublisher.publishEvent(new PaymentInitiatedEvent(this, payment.getId()));

        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(user.getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod().name())
                .status(payment.getStatus().name())
                .hash(hashOrUrl)
                .build();
    }

    @Override
    @Transactional
    public void verifyPayment(UUID paymentId, PaymentVerificationRequest request) {
        log.info("Verifying payment: {}", paymentId);
        
        Payment payment = getPaymentEntity(paymentId);
        
        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new BusinessException("Only pending payments can be verified");
        }

        PaymentTransaction transaction = paymentTransactionRepository.findAll().stream()
                .filter(t -> t.getPayment().getId().equals(paymentId) && t.getStatus() == PaymentStatus.PENDING)
                .findFirst()
                .orElseThrow(() -> new BusinessException("No pending transaction found for payment"));

        PaymentGateway gateway = gatewayFactory.getGateway(payment.getPaymentMethod());
        boolean isSuccess = gateway.verifyPayment(transaction, request.getGatewayTransactionId());

        if (isSuccess) {
            markPaymentSuccess(payment, transaction, request.getGatewayTransactionId());
        } else {
            markPaymentFailed(payment, transaction);
        }
    }

    @Override
    @Transactional
    public void processWebhook(WebhookRequest request) {
        // Normally, order_id contains the payment ID or transaction ID. Assuming it's payment ID here.
        UUID paymentId = UUID.fromString(request.getOrder_id());
        Payment payment = getPaymentEntity(paymentId);

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return; // already processed
        }

        PaymentTransaction transaction = paymentTransactionRepository.findAll().stream()
                .filter(t -> t.getPayment().getId().equals(paymentId))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Transaction not found"));

        PaymentGateway gateway = gatewayFactory.getGateway(payment.getPaymentMethod());
        boolean isSuccess = gateway.processWebhook(transaction, request);

        if (isSuccess) {
            markPaymentSuccess(payment, transaction, request.getPayment_id());
        } else {
            markPaymentFailed(payment, transaction);
        }
    }

    private void markPaymentSuccess(Payment payment, PaymentTransaction transaction, String externalId) {
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        transaction.setStatus(PaymentStatus.SUCCESS);
        transaction.setGatewayTransactionId(externalId);
        paymentTransactionRepository.save(transaction);

        Invoice invoice = invoiceRepository.findByPaymentIdAndDeletedAtIsNull(payment.getId())
                .orElseThrow(() -> new BusinessException("Invoice not found for payment"));
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

        eventPublisher.publishEvent(new PaymentCompletedEvent(this, payment.getId()));
        eventPublisher.publishEvent(new ReceiptGeneratedEvent(this, invoice.getId()));
    }

    private void markPaymentFailed(Payment payment, PaymentTransaction transaction) {
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        transaction.setStatus(PaymentStatus.FAILED);
        paymentTransactionRepository.save(transaction);

        eventPublisher.publishEvent(new PaymentFailedEvent(this, payment.getId()));
    }

    @Override
    @Transactional
    public void cancelPayment(UUID paymentId) {
        Payment payment = getPaymentEntity(paymentId);
        if (payment.getStatus() == PaymentStatus.SUCCESS || payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new BusinessException("Cannot cancel a completed or refunded payment");
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentCancelledEvent(this, payment.getId()));
    }

    @Override
    @Transactional
    public void refundPayment(UUID paymentId) {
        Payment payment = getPaymentEntity(paymentId);
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BusinessException("Only successful payments can be refunded");
        }

        PaymentTransaction transaction = paymentTransactionRepository.findAll().stream()
                .filter(t -> t.getPayment().getId().equals(paymentId) && t.getStatus() == PaymentStatus.SUCCESS)
                .findFirst()
                .orElseThrow(() -> new BusinessException("Success transaction not found"));

        PaymentGateway gateway = gatewayFactory.getGateway(payment.getPaymentMethod());
        boolean refunded = gateway.refundPayment(transaction);

        if (refunded) {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
            
            Invoice invoice = invoiceRepository.findByPaymentIdAndDeletedAtIsNull(payment.getId())
                    .orElseThrow(() -> new BusinessException("Invoice not found"));
            invoice.setStatus(InvoiceStatus.VOID);
            invoiceRepository.save(invoice);
            
            eventPublisher.publishEvent(new PaymentRefundedEvent(this, payment.getId()));
        } else {
            throw new BusinessException("Gateway refund failed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentDetails(UUID paymentId) {
        Payment payment = getPaymentEntity(paymentId);
        return PaymentResponse.builder()
                .id(payment.getId())
                .userId(payment.getUser().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod().name())
                .status(payment.getStatus().name())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSummaryResponse> searchPayments(SearchPaymentRequest request, Pageable pageable) {
        Page<Payment> payments = paymentRepository.findAll(PaymentSpecification.filterBy(request), pageable);
        return payments.map(this::mapToSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentSummaryResponse> getPaymentHistory(UUID userId, Pageable pageable) {
        SearchPaymentRequest request = new SearchPaymentRequest();
        request.setUserId(userId);
        return searchPayments(request, pageable);
    }

    private Payment getPaymentEntity(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
    }

    private PaymentSummaryResponse mapToSummary(Payment payment) {
        return PaymentSummaryResponse.builder()
                .id(payment.getId())
                .studentName(payment.getUser().getFirstName() + " " + payment.getUser().getLastName())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod().name())
                .status(payment.getStatus().name())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
