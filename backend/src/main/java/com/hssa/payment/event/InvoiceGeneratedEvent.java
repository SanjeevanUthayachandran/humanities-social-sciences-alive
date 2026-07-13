package com.hssa.payment.event;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class InvoiceGeneratedEvent extends ApplicationEvent {
    private final UUID invoiceId;
    public InvoiceGeneratedEvent(Object source, UUID invoiceId) { 
        super(source); 
        this.invoiceId = invoiceId;
    }
}
