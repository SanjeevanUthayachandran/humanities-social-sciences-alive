package com.hssa.certificate.event;
import java.util.UUID;
public class CertificateDownloadedEvent extends CertificateEvent {
    public CertificateDownloadedEvent(Object source, UUID certificateId) { super(source, certificateId); }
}
