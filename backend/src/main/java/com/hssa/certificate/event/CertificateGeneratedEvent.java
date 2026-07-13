package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateGeneratedEvent extends CertificateEvent {
    public CertificateGeneratedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
