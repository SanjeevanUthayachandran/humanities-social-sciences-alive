package com.hssa.payment.event;
import java.util.UUID;
public class PaymentCancelledEvent extends PaymentEvent {
    public PaymentCancelledEvent(Object source, UUID paymentId) { super(source, paymentId); }
}
