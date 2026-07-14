package com.hssa.contact.controller;

import com.hssa.contact.dto.request.FAQRequest;
import com.hssa.contact.dto.response.FAQResponse;
import com.hssa.contact.service.ContactFaqService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact/faqs")
@RequiredArgsConstructor
public class ContactFaqController {

    private final ContactFaqService faqService;

    @GetMapping
    public ResponseEntity<List<FAQResponse>> getAllActiveFaqs() {
        return ResponseEntity.ok(faqService.getAllActiveFaqs());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<FAQResponse> createFaq(@Valid @RequestBody FAQRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(faqService.createFaq(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<FAQResponse> updateFaq(
            @PathVariable UUID id,
            @Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqService.updateFaq(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteFaq(@PathVariable UUID id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}