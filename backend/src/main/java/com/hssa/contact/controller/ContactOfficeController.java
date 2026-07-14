package com.hssa.contact.controller;

import com.hssa.contact.dto.request.ContactOfficeRequest;
import com.hssa.contact.dto.response.ContactOfficeResponse;
import com.hssa.contact.service.ContactOfficeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact/offices")
@RequiredArgsConstructor
public class ContactOfficeController {

    private final ContactOfficeService officeService;

    @GetMapping
    public ResponseEntity<List<ContactOfficeResponse>> getAllActiveOffices() {
        return ResponseEntity.ok(officeService.getAllActiveOffices());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactOfficeResponse> createOffice(@Valid @RequestBody ContactOfficeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(officeService.createOffice(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactOfficeResponse> updateOffice(
            @PathVariable UUID id,
            @Valid @RequestBody ContactOfficeRequest request) {
        return ResponseEntity.ok(officeService.updateOffice(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOffice(@PathVariable UUID id) {
        officeService.deleteOffice(id);
        return ResponseEntity.noContent().build();
    }
}