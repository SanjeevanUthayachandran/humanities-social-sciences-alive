package com.hssa.contact.repository;

import com.hssa.contact.entity.ContactFaq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactFaqRepository extends JpaRepository<ContactFaq, UUID> {
    List<ContactFaq> findByIsActiveTrueOrderByDisplayOrderAsc();
}