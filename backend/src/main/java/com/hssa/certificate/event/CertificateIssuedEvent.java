package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateIssuedEvent extends CertificateEvent {
    public CertificateIssuedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
