package com.hssa.payment.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class PaymentEvent extends ApplicationEvent {
    private final UUID paymentId;
    public PaymentEvent(Object source, UUID paymentId) {
        super(source);
        this.paymentId = paymentId;
    }
}
