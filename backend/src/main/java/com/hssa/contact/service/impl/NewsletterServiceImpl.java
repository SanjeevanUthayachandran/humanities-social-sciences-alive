package com.hssa.contact.service.impl;

import com.hssa.contact.entity.NewsletterSubscriber;
import com.hssa.contact.repository.NewsletterSubscriberRepository;
import com.hssa.contact.service.NewsletterService;
import com.hssa.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterSubscriberRepository subscriberRepository;

    @Override
    @Transactional
    public void subscribe(String email, String fullName) {
        NewsletterSubscriber subscriber = subscriberRepository.findByEmail(email).orElse(null);
        if (subscriber != null) {
            if (!subscriber.getIsActive()) {
                subscriber.setIsActive(true);
                subscriber.setSubscribedAt(LocalDateTime.now());
                subscriber.setUnsubscribedAt(null);
                if (fullName != null) {
                    subscriber.setFullName(fullName);
                }
                subscriberRepository.save(subscriber);
            }
            return;
        }

        subscriber = NewsletterSubscriber.builder()
                .email(email)
                .fullName(fullName)
                .isActive(true)
                .subscribedAt(LocalDateTime.now())
                .build();
        subscriberRepository.save(subscriber);
    }

    @Override
    @Transactional
    public void unsubscribe(String email) {
        NewsletterSubscriber subscriber = subscriberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Subscriber not found"));
        if (subscriber.getIsActive()) {
            subscriber.setIsActive(false);
            subscriber.setUnsubscribedAt(LocalDateTime.now());
            subscriberRepository.save(subscriber);
        }
    }
}