package com.hssa.payment.event;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class ReceiptGeneratedEvent extends ApplicationEvent {
    private final UUID invoiceId;
    public ReceiptGeneratedEvent(Object source, UUID invoiceId) { 
        super(source); 
        this.invoiceId = invoiceId;
    }
}
