package com.hssa.contact.service.impl;

import com.hssa.contact.dto.request.FAQRequest;
import com.hssa.contact.dto.response.FAQResponse;
import com.hssa.contact.entity.ContactFaq;
import com.hssa.contact.repository.ContactFaqRepository;
import com.hssa.contact.service.ContactFaqService;
import com.hssa.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactFaqServiceImpl implements ContactFaqService {

    private final ContactFaqRepository faqRepository;

    @Override
    @Transactional
    public FAQResponse createFaq(FAQRequest request) {
        ContactFaq faq = ContactFaq.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        faq = faqRepository.save(faq);
        return buildResponse(faq);
    }

    @Override
    @Transactional
    public FAQResponse updateFaq(UUID id, FAQRequest request) {
        ContactFaq faq = faqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FAQ not found"));
        if (request.getQuestion() != null) faq.setQuestion(request.getQuestion());
        if (request.getAnswer() != null) faq.setAnswer(request.getAnswer());
        if (request.getDisplayOrder() != null) faq.setDisplayOrder(request.getDisplayOrder());
        if (request.getIsActive() != null) faq.setIsActive(request.getIsActive());
        faq = faqRepository.save(faq);
        return buildResponse(faq);
    }

    @Override
    @Transactional
    public void deleteFaq(UUID id) {
        if (!faqRepository.existsById(id)) {
            throw new ResourceNotFoundException("FAQ not found");
        }
        faqRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FAQResponse> getAllActiveFaqs() {
        return faqRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    private FAQResponse buildResponse(ContactFaq faq) {
        return FAQResponse.builder()
                .id(faq.getId())
                .question(faq.getQuestion())
                .answer(faq.getAnswer())
                .displayOrder(faq.getDisplayOrder())
                .isActive(faq.getIsActive())
                .build();
    }
}