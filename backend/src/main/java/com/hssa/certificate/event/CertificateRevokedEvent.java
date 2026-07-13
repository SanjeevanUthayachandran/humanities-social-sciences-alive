package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateRevokedEvent extends CertificateEvent {
    public CertificateRevokedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
