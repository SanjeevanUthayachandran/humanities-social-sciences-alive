package com.hssa.contact.service;

import com.hssa.contact.dto.request.ContactOfficeRequest;
import com.hssa.contact.dto.response.ContactOfficeResponse;

import java.util.List;
import java.util.UUID;

public interface ContactOfficeService {
    ContactOfficeResponse createOffice(ContactOfficeRequest request);
    ContactOfficeResponse updateOffice(UUID id, ContactOfficeRequest request);
    void deleteOffice(UUID id);
    List<ContactOfficeResponse> getAllActiveOffices();
}