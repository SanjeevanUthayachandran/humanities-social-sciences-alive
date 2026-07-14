package com.hssa.contact.repository;

import com.hssa.contact.entity.ContactOffice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContactOfficeRepository extends JpaRepository<ContactOffice, UUID> {
    List<ContactOffice> findByIsActiveTrue();
}