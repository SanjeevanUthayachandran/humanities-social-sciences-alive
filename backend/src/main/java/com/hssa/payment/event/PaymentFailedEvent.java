package com.hssa.payment.event;
import java.util.UUID;
public class PaymentFailedEvent extends PaymentEvent {
    public PaymentFailedEvent(Object source, UUID paymentId) { super(source, paymentId); }
}
