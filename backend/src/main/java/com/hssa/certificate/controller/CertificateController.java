package com.hssa.certificate.controller;

import com.hssa.certificate.dto.request.GenerateCertificateRequest;
import com.hssa.certificate.dto.request.ReissueCertificateRequest;
import com.hssa.certificate.dto.request.RevokeCertificateRequest;
import com.hssa.certificate.dto.response.CertificateResponse;
import com.hssa.certificate.dto.response.CertificateSummaryResponse;
import com.hssa.certificate.dto.response.CertificateVerificationResponse;
import com.hssa.certificate.service.CertificateService;
import com.hssa.certificate.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final VerificationService verificationService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<CertificateResponse> generateCertificate(@Valid @RequestBody GenerateCertificateRequest request) {
        return ResponseEntity.ok(certificateService.generateCertificate(request));
    }

    @PostMapping("/reissue")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CertificateResponse> reissueCertificate(@Valid @RequestBody ReissueCertificateRequest request) {
        return ResponseEntity.ok(certificateService.reissueCertificate(request));
    }

    @PatchMapping("/{id}/revoke")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> revokeCertificate(
            @PathVariable UUID id,
            @Valid @RequestBody RevokeCertificateRequest request) {
        certificateService.revokeCertificate(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<CertificateResponse> getCertificate(@PathVariable UUID id) {
        return ResponseEntity.ok(certificateService.getCertificate(id));
    }

    @GetMapping("/verify/{certificateNumber}")
    public ResponseEntity<CertificateVerificationResponse> verifyCertificate(@PathVariable String certificateNumber) {
        return ResponseEntity.ok(verificationService.verifyCertificateByNumber(certificateNumber));
    }

    @GetMapping("/verify/code/{verificationCode}")
    public ResponseEntity<CertificateVerificationResponse> verifyCertificateByCode(@PathVariable String verificationCode) {
        return ResponseEntity.ok(verificationService.verifyCertificateByCode(verificationCode));
    }

    @GetMapping("/download/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable UUID id) {
        byte[] pdfBytes = certificateService.downloadCertificate(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate-" + id + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/preview/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<byte[]> previewCertificate(@PathVariable UUID id) {
        byte[] pdfBytes = certificateService.previewCertificate(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("inline", "certificate-" + id + ".pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTRUCTOR')")
    public ResponseEntity<Page<CertificateSummaryResponse>> searchCertificates(
            @RequestParam(required = false) UUID studentId,
            @RequestParam(required = false) UUID courseId,
            @RequestParam(required = false) String certificateNumber,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(certificateService.searchCertificates(studentId, courseId, certificateNumber, status, pageable));
    }
}
