package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateReissuedEvent extends CertificateEvent {
    public CertificateReissuedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
