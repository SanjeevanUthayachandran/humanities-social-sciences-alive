package com.hssa.contact.service;

import com.hssa.contact.dto.request.ContactRequest;
import com.hssa.contact.dto.request.ReplyRequest;
import com.hssa.contact.dto.response.ContactResponse;
import com.hssa.contact.dto.response.ContactStatisticsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContactService {
    ContactResponse submitContactMessage(ContactRequest request, String ipAddress, String userAgent);
    ContactResponse getMessage(UUID id);
    void assignMessage(UUID id, UUID assigneeId, UUID assignerId);
    void resolveMessage(UUID id, UUID resolverId);
    void replyToMessage(UUID id, ReplyRequest request, UUID replierId);
    Page<ContactResponse> searchMessages(String status, String priority, String inquiryType, String keyword, Pageable pageable);
    ContactStatisticsResponse getStatistics();
}