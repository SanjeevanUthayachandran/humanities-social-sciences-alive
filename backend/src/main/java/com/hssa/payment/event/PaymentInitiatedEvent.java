package com.hssa.payment.event;
import java.util.UUID;
public class PaymentInitiatedEvent extends PaymentEvent {
    public PaymentInitiatedEvent(Object source, UUID paymentId) { super(source, paymentId); }
}
