package com.hssa.certificate.generator;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class DummyQrCodeGenerator implements QrCodeGenerator {
    @Override
    public byte[] generateQrCode(String verificationUrl) {
        String dummyImage = "<svg height=\"100\" width=\"100\"><circle cx=\"50\" cy=\"50\" r=\"40\" stroke=\"black\" stroke-width=\"3\" fill=\"red\" /><text x=\"20\" y=\"50\" fill=\"white\">QR:" + verificationUrl + "</text></svg>";
        return dummyImage.getBytes(StandardCharsets.UTF_8);
    }
}
