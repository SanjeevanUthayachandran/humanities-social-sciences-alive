package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateVerifiedEvent extends CertificateEvent {
    public CertificateVerifiedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
