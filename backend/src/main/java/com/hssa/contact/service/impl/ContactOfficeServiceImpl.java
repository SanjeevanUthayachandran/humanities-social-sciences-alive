package com.hssa.contact.service.impl;

import com.hssa.contact.dto.request.ContactOfficeRequest;
import com.hssa.contact.dto.response.ContactOfficeResponse;
import com.hssa.contact.entity.ContactOffice;
import com.hssa.contact.repository.ContactOfficeRepository;
import com.hssa.contact.service.ContactOfficeService;
import com.hssa.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactOfficeServiceImpl implements ContactOfficeService {

    private final ContactOfficeRepository officeRepository;

    @Override
    @Transactional
    public ContactOfficeResponse createOffice(ContactOfficeRequest request) {
        ContactOffice office = ContactOffice.builder()
                .officeName(request.getOfficeName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .openingHours(request.getOpeningHours())
                .googleMapUrl(request.getGoogleMapUrl())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .isHeadOffice(request.getIsHeadOffice() != null ? request.getIsHeadOffice() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();
        office = officeRepository.save(office);
        return buildResponse(office);
    }

    @Override
    @Transactional
    public ContactOfficeResponse updateOffice(UUID id, ContactOfficeRequest request) {
        ContactOffice office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Office not found"));
        if (request.getOfficeName() != null) office.setOfficeName(request.getOfficeName());
        if (request.getAddress() != null) office.setAddress(request.getAddress());
        if (request.getPhone() != null) office.setPhone(request.getPhone());
        if (request.getEmail() != null) office.setEmail(request.getEmail());
        if (request.getOpeningHours() != null) office.setOpeningHours(request.getOpeningHours());
        if (request.getGoogleMapUrl() != null) office.setGoogleMapUrl(request.getGoogleMapUrl());
        if (request.getLatitude() != null) office.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) office.setLongitude(request.getLongitude());
        if (request.getIsHeadOffice() != null) office.setIsHeadOffice(request.getIsHeadOffice());
        if (request.getIsActive() != null) office.setIsActive(request.getIsActive());
        office = officeRepository.save(office);
        return buildResponse(office);
    }

    @Override
    @Transactional
    public void deleteOffice(UUID id) {
        if (!officeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Office not found");
        }
        officeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactOfficeResponse> getAllActiveOffices() {
        return officeRepository.findByIsActiveTrue()
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }

    private ContactOfficeResponse buildResponse(ContactOffice office) {
        return ContactOfficeResponse.builder()
                .id(office.getId())
                .officeName(office.getOfficeName())
                .address(office.getAddress())
                .phone(office.getPhone())
                .email(office.getEmail())
                .openingHours(office.getOpeningHours())
                .googleMapUrl(office.getGoogleMapUrl())
                .latitude(office.getLatitude())
                .longitude(office.getLongitude())
                .isHeadOffice(office.getIsHeadOffice())
                .isActive(office.getIsActive())
                .build();
    }
}