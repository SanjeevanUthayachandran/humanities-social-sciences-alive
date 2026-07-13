package com.hssa.certificate.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import java.util.UUID;

@Getter
public class CertificateEvent extends ApplicationEvent {
    private final UUID certificateId;
    public CertificateEvent(Object source, UUID certificateId) {
        super(source);
        this.certificateId = certificateId;
    }
}
