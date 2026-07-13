package com.hssa.certificate.generator;

public interface QrCodeGenerator {
    byte[] generateQrCode(String verificationUrl);
}
