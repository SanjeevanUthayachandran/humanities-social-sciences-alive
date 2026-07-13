package com.hssa.payment.event;
import java.util.UUID;
public class PaymentCompletedEvent extends PaymentEvent {
    public PaymentCompletedEvent(Object source, UUID paymentId) { super(source, paymentId); }
}
