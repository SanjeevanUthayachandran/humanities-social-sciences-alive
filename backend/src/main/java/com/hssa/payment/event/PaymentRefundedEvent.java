package com.hssa.payment.event;
import java.util.UUID;
public class PaymentRefundedEvent extends PaymentEvent {
    public PaymentRefundedEvent(Object source, UUID paymentId) { super(source, paymentId); }
}
