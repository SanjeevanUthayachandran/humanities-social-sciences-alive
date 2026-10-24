package com.hssa.publication.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.publication.dto.request.AddAuthorRequest;
import com.hssa.publication.dto.request.CreatePublicationRequest;
import com.hssa.publication.dto.request.UpdatePublicationRequest;
import com.hssa.publication.dto.response.PublicationDetailResponse;
import com.hssa.publication.dto.response.PublicationAuthorResponse;
import com.hssa.publication.dto.response.PublicationSummaryResponse;
import com.hssa.publication.dto.response.PublicationStatisticsResponse;
import com.hssa.publication.entity.*;
import com.hssa.publication.enums.PublicationStatus;
import com.hssa.publication.event.*;
import com.hssa.publication.repository.*;
import com.hssa.publication.service.PublicationService;
import com.hssa.publication.specification.PublicationSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicationServiceImpl implements PublicationService {

    private final PublicationRepository publicationRepository;
    private final PublicationCategoryRepository categoryRepository;
    private final PublicationAuthorRepository authorRepository;
    private final PublicationKeywordRepository keywordRepository;
    private final PublicationPublicationKeywordRepository publicationKeywordRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public PublicationDetailResponse createPublication(CreatePublicationRequest request, UUID creatorId) {
        if (publicationRepository.existsByTitleAndDeletedAtIsNull(request.getTitle())) {
            throw new BusinessException("Publication title must be unique");
        }

        PublicationCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String slug = request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-");

        Publication publication = Publication.builder()
                .title(request.getTitle())
                .slug(slug)
                .category(category)
                .abstractText(request.getAbstractText())
                .content(request.getContent())
                .publicationType(request.getPublicationType())
                .status(PublicationStatus.DRAFT)
                .coverMediaId(request.getCoverMediaId())
                .doi(request.getDoi())
                .build();
        
        publication = publicationRepository.save(publication);

        User creator = userRepository.findById(creatorId).orElseThrow(() -> new ResourceNotFoundException("Creator not found"));
        PublicationAuthor leadAuthor = PublicationAuthor.builder()
                .publication(publication)
                .user(creator)
                .isPrimary(true)
                .displayOrder(0)
                .build();
        authorRepository.save(leadAuthor);

        syncKeywords(publication, request.getKeywords());

        eventPublisher.publishEvent(new PublicationCreatedEvent(this, publication.getId()));

        return buildDetailResponse(publication);
    }

    @Override
    @Transactional
    public PublicationDetailResponse updatePublication(UUID publicationId, UpdatePublicationRequest request) {
        Publication publication = publicationRepository.findByIdAndDeletedAtIsNull(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));

        if (request.getTitle() != null && !request.getTitle().equals(publication.getTitle())) {
            if (publicationRepository.existsByTitleAndDeletedAtIsNull(request.getTitle())) {
                throw new BusinessException("Publication title must be unique");
            }
            publication.setTitle(request.getTitle());
            publication.setSlug(request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        }

        if (request.getCategoryId() != null) {
            PublicationCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            publication.setCategory(category);
        }

        if (request.getAbstractText() != null) publication.setAbstractText(request.getAbstractText());
        if (request.getContent() != null) publication.setContent(request.getContent());
        if (request.getPublicationType() != null) publication.setPublicationType(request.getPublicationType());
        if (request.getCoverMediaId() != null) publication.setCoverMediaId(request.getCoverMediaId());
        if (request.getDoi() != null) publication.setDoi(request.getDoi());

        if (request.getKeywords() != null) {
            syncKeywords(publication, request.getKeywords());
        }

        publication = publicationRepository.save(publication);
        eventPublisher.publishEvent(new PublicationUpdatedEvent(this, publication.getId()));

        return buildDetailResponse(publication);
    }

    @Override
    @Transactional
    public void deletePublication(UUID publicationId) {
        Publication publication = publicationRepository.findByIdAndDeletedAtIsNull(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        
        publication.setDeletedAt(LocalDateTime.now());
        publicationRepository.save(publication);

        eventPublisher.publishEvent(new PublicationDeletedEvent(this, publication.getId()));
    }

    @Override
    @Transactional
    public void archivePublication(UUID publicationId) {
        Publication publication = publicationRepository.findByIdAndDeletedAtIsNull(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        
        publication.setStatus(PublicationStatus.ARCHIVED);
        publicationRepository.save(publication);

        eventPublisher.publishEvent(new PublicationArchivedEvent(this, publication.getId()));
    }

    @Override
    @Transactional
    public void restorePublication(UUID publicationId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        
        publication.setDeletedAt(null);
        publicationRepository.save(publication);
        
        eventPublisher.publishEvent(new PublicationUpdatedEvent(this, publication.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationDetailResponse getPublication(UUID publicationId) {
        Publication publication = publicationRepository.findByIdAndDeletedAtIsNull(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        
        return buildDetailResponse(publication);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationSummaryResponse> listPublications(Pageable pageable) {
        return publicationRepository.findAll(
                PublicationSpecification.filterBy(null, null, null), pageable)
                .map(p -> PublicationSummaryResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .slug(p.getSlug())
                        .abstractText(p.getAbstractText())
                        .categoryName(p.getCategory().getName())
                        .status(p.getStatus().name())
                        .publicationType(p.getPublicationType().name())
                        .publishedDate(p.getPublishedDate())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PublicationSummaryResponse> searchPublication(String title, String category, String status, String doi, Pageable pageable) {
        Page<Publication> publications = publicationRepository.findAll(
                PublicationSpecification.filterBy(title, status, doi), pageable);

        return publications.map(p -> PublicationSummaryResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .abstractText(p.getAbstractText())
                .categoryName(p.getCategory().getName())
                .status(p.getStatus().name())
                .publicationType(p.getPublicationType().name())
                .publishedDate(p.getPublishedDate())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PublicationAuthorResponse> getAuthors(UUID publicationId) {
        return authorRepository.findByPublicationId(publicationId).stream()
                .map(a -> PublicationAuthorResponse.builder()
                        .userId(a.getUser().getId())
                        .firstName(a.getUser().getFirstName())
                        .lastName(a.getUser().getLastName())
                        .isPrimary(a.getIsPrimary())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addAuthor(UUID publicationId, AddAuthorRequest request) {
        Publication publication = publicationRepository.findByIdAndDeletedAtIsNull(publicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Publication not found"));
        
        if (authorRepository.existsByPublicationIdAndUserId(publicationId, request.getUserId())) {
            throw new BusinessException("User is already an author of this publication");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PublicationAuthor author = PublicationAuthor.builder()
                .publication(publication)
                .user(user)
                .isPrimary(request.isPrimary())
                .build();
        authorRepository.save(author);

        eventPublisher.publishEvent(new AuthorAddedEvent(this, publication.getId(), user.getId()));
    }

    @Override
    @Transactional
    public void removeAuthor(UUID publicationId, UUID authorId) {
        PublicationAuthor author = authorRepository.findByPublicationIdAndUserId(publicationId, authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found in this publication"));
        
        authorRepository.delete(author);

        eventPublisher.publishEvent(new AuthorRemovedEvent(this, publicationId, authorId));
    }

    @Override
    @Transactional(readOnly = true)
    public PublicationStatisticsResponse getStatistics() {
        long total = publicationRepository.count();
        long drafts = publicationRepository.findAll(PublicationSpecification.filterBy(null, "DRAFT", null), Pageable.unpaged()).getTotalElements();
        long published = publicationRepository.findAll(PublicationSpecification.filterBy(null, "PUBLISHED", null), Pageable.unpaged()).getTotalElements();
        long archived = publicationRepository.findAll(PublicationSpecification.filterBy(null, "ARCHIVED", null), Pageable.unpaged()).getTotalElements();
        return PublicationStatisticsResponse.builder()
                .totalPublications(total)
                .totalDrafts(drafts)
                .totalPublished(published)
                .totalArchived(archived)
                .build();
    }

    private void syncKeywords(Publication publication, List<String> keywords) {
        if (keywords == null) return;
        
        publicationKeywordRepository.findByPublicationId(publication.getId()).forEach(km -> {
            publicationKeywordRepository.deleteByPublicationIdAndKeywordId(publication.getId(), km.getKeyword().getId());
        });

        for (String kw : keywords) {
            String sanitizedKw = kw.trim().toLowerCase();
            PublicationKeyword keyword = keywordRepository.findByKeywordIgnoreCaseAndDeletedAtIsNull(sanitizedKw)
                    .orElseGet(() -> {
                        PublicationKeyword newKw = PublicationKeyword.builder().keyword(sanitizedKw).build();
                        return keywordRepository.save(newKw);
                    });

            PublicationPublicationKeyword map = PublicationPublicationKeyword.builder()
                    .publication(publication)
                    .keyword(keyword)
                    .build();
            publicationKeywordRepository.save(map);
        }
    }

    private PublicationDetailResponse buildDetailResponse(Publication publication) {
        List<PublicationAuthorResponse> authors = getAuthors(publication.getId());
        List<String> keywords = publicationKeywordRepository.findByPublicationId(publication.getId()).stream()
                .map(km -> km.getKeyword().getKeyword())
                .collect(Collectors.toList());

        return PublicationDetailResponse.builder()
                .id(publication.getId())
                .title(publication.getTitle())
                .slug(publication.getSlug())
                .abstractText(publication.getAbstractText())
                .content(publication.getContent())
                .categoryName(publication.getCategory().getName())
                .status(publication.getStatus().name())
                .publicationType(publication.getPublicationType().name())
                .coverMediaId(publication.getCoverMediaId())
                .publishedDate(publication.getPublishedDate())
                .doi(publication.getDoi())
                .keywords(keywords)
                .authors(authors)
                .build();
    }
}
