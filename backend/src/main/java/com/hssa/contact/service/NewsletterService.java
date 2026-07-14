package com.hssa.contact.service;

public interface NewsletterService {
    void subscribe(String email, String fullName);
    void unsubscribe(String email);
}