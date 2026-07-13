package com.hssa.certificate.generator;

import com.hssa.certificate.entity.Certificate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class DummyPdfCertificateGenerator implements CertificateGenerator {
    @Override
    public byte[] generateCertificate(Certificate certificate) {
        // Return a dummy PDF content based on string
        String dummyPdf = "%PDF-1.4\n" +
                "1 0 obj\n" +
                "<< /Type /Catalog /Pages 2 0 R >>\n" +
                "endobj\n" +
                "2 0 obj\n" +
                "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                "endobj\n" +
                "3 0 obj\n" +
                "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\n" +
                "endobj\n" +
                "4 0 obj\n" +
                "<< /Length 73 >>\n" +
                "stream\n" +
                "BT /F1 24 Tf 100 700 Td (Certificate of Completion) Tj ET\n" +
                "BT /F1 18 Tf 100 650 Td (" + certificate.getCertificateNumber() + ") Tj ET\n" +
                "endstream\n" +
                "endobj\n" +
                "5 0 obj\n" +
                "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n" +
                "endobj\n" +
                "xref\n" +
                "0 6\n" +
                "0000000000 65535 f \n" +
                "0000000009 00000 n \n" +
                "0000000058 00000 n \n" +
                "0000000115 00000 n \n" +
                "0000000243 00000 n \n" +
                "0000000367 00000 n \n" +
                "trailer\n" +
                "<< /Size 6 /Root 1 0 R >>\n" +
                "startxref\n" +
                "455\n" +
                "%%EOF";
        return dummyPdf.getBytes(StandardCharsets.UTF_8);
    }
}
