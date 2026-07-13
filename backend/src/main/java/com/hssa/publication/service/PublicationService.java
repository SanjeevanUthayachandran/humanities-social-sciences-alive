package com.hssa.publication.service;

import com.hssa.publication.dto.request.CreatePublicationRequest;
import com.hssa.publication.dto.request.UpdatePublicationRequest;
import com.hssa.publication.dto.request.AddAuthorRequest;
import com.hssa.publication.dto.response.PublicationDetailResponse;
import com.hssa.publication.dto.response.PublicationAuthorResponse;
import com.hssa.publication.dto.response.PublicationSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface PublicationService {
    PublicationDetailResponse createPublication(CreatePublicationRequest request, UUID creatorId);
    PublicationDetailResponse updatePublication(UUID publicationId, UpdatePublicationRequest request);
    void deletePublication(UUID publicationId);
    void archivePublication(UUID publicationId);
    void restorePublication(UUID publicationId);
    PublicationDetailResponse getPublication(UUID publicationId);
    Page<PublicationSummaryResponse> searchPublication(String title, String category, String status, String doi, Pageable pageable);
    
    // Author Management
    List<PublicationAuthorResponse> getAuthors(UUID publicationId);
    void addAuthor(UUID publicationId, AddAuthorRequest request);
    void removeAuthor(UUID publicationId, UUID authorId);
}
