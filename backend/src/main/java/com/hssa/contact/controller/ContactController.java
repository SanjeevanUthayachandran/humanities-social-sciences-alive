package com.hssa.contact.controller;

import com.hssa.security.dto.CustomUserDetails;
import com.hssa.contact.dto.request.ContactRequest;
import com.hssa.contact.dto.request.ReplyRequest;
import com.hssa.contact.dto.response.ContactResponse;
import com.hssa.contact.dto.response.ContactStatisticsResponse;
import com.hssa.contact.service.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping("/messages")
    public ResponseEntity<ContactResponse> submitContactMessage(
            @Valid @RequestBody ContactRequest request,
            HttpServletRequest servletRequest) {
        String ipAddress = servletRequest.getRemoteAddr();
        String userAgent = servletRequest.getHeader("User-Agent");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactService.submitContactMessage(request, ipAddress, userAgent));
    }

    @GetMapping("/messages")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<Page<ContactResponse>> searchMessages(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String inquiryType,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(contactService.searchMessages(status, priority, inquiryType, keyword, pageable));
    }

    @GetMapping("/messages/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<ContactResponse> getMessage(@PathVariable UUID id) {
        return ResponseEntity.ok(contactService.getMessage(id));
    }

    @PatchMapping("/messages/{id}/assign")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<Void> assignMessage(
            @PathVariable UUID id,
            @RequestParam UUID assigneeId,
            @AuthenticationPrincipal CustomUserDetails assigner) {
        contactService.assignMessage(id, assigneeId, assigner.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{id}/resolve")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<Void> resolveMessage(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails resolver) {
        contactService.resolveMessage(id, resolver.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/messages/{id}/reply")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<Void> replyToMessage(
            @PathVariable UUID id,
            @Valid @RequestBody ReplyRequest request,
            @AuthenticationPrincipal CustomUserDetails replier) {
        contactService.replyToMessage(id, request, replier.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPPORT')")
    public ResponseEntity<ContactStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(contactService.getStatistics());
    }
}
