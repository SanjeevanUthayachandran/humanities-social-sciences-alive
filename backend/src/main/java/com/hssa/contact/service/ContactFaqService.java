package com.hssa.contact.service;

import com.hssa.contact.dto.request.FAQRequest;
import com.hssa.contact.dto.response.FAQResponse;

import java.util.List;
import java.util.UUID;

public interface ContactFaqService {
    FAQResponse createFaq(FAQRequest request);
    FAQResponse updateFaq(UUID id, FAQRequest request);
    void deleteFaq(UUID id);
    List<FAQResponse> getAllActiveFaqs();
}