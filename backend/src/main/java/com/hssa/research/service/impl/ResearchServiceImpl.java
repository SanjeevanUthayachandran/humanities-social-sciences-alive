package com.hssa.research.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.research.dto.request.AddMemberRequest;
import com.hssa.research.dto.request.CreateResearchRequest;
import com.hssa.research.dto.request.UpdateResearchRequest;
import com.hssa.research.dto.response.ResearchDetailResponse;
import com.hssa.research.dto.response.ResearchMemberResponse;
import com.hssa.research.dto.response.ResearchSummaryResponse;
import com.hssa.research.entity.*;
import com.hssa.research.enums.ResearchStatus;
import com.hssa.research.event.*;
import com.hssa.research.repository.*;
import com.hssa.research.service.ResearchService;
import com.hssa.research.specification.ResearchSpecification;
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
public class ResearchServiceImpl implements ResearchService {

    private final ResearchProjectRepository projectRepository;
    private final ResearchCategoryRepository categoryRepository;
    private final ResearchAuthorRepository authorRepository;
    private final ResearchKeywordRepository keywordRepository;
    private final ResearchProjectKeywordRepository projectKeywordRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public ResearchDetailResponse createResearch(CreateResearchRequest request, UUID creatorId) {
        if (projectRepository.existsByTitleAndDeletedAtIsNull(request.getTitle())) {
            throw new BusinessException("Research title must be unique");
        }

        ResearchCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        String slug = request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-");

        ResearchProject project = ResearchProject.builder()
                .title(request.getTitle())
                .slug(slug)
                .category(category)
                .abstractText(request.getAbstractText())
                .content(request.getContent())
                .researchType(request.getResearchType())
                .status(ResearchStatus.DRAFT)
                .coverMediaId(request.getCoverMediaId())
                .build();
        
        project = projectRepository.save(project);

        User creator = userRepository.findById(creatorId).orElseThrow(() -> new ResourceNotFoundException("Creator not found"));
        ResearchAuthor leadAuthor = ResearchAuthor.builder()
                .project(project)
                .user(creator)
                .isPrimary(true)
                .displayOrder(0)
                .build();
        authorRepository.save(leadAuthor);

        syncKeywords(project, request.getKeywords());

        eventPublisher.publishEvent(new ResearchCreatedEvent(this, project.getId()));

        return buildDetailResponse(project);
    }

    @Override
    @Transactional
    public ResearchDetailResponse updateResearch(UUID projectId, UpdateResearchRequest request) {
        ResearchProject project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));

        if (request.getTitle() != null && !request.getTitle().equals(project.getTitle())) {
            if (projectRepository.existsByTitleAndDeletedAtIsNull(request.getTitle())) {
                throw new BusinessException("Research title must be unique");
            }
            project.setTitle(request.getTitle());
            project.setSlug(request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-"));
        }

        if (request.getCategoryId() != null) {
            ResearchCategory category = categoryRepository.findByIdAndDeletedAtIsNull(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            project.setCategory(category);
        }

        if (request.getAbstractText() != null) project.setAbstractText(request.getAbstractText());
        if (request.getContent() != null) project.setContent(request.getContent());
        if (request.getResearchType() != null) project.setResearchType(request.getResearchType());
        if (request.getCoverMediaId() != null) project.setCoverMediaId(request.getCoverMediaId());

        if (request.getKeywords() != null) {
            syncKeywords(project, request.getKeywords());
        }

        project = projectRepository.save(project);
        eventPublisher.publishEvent(new ResearchUpdatedEvent(this, project.getId()));

        return buildDetailResponse(project);
    }

    @Override
    @Transactional
    public void deleteResearch(UUID projectId) {
        ResearchProject project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));
        
        project.setDeletedAt(LocalDateTime.now());
        projectRepository.save(project);

        eventPublisher.publishEvent(new ResearchDeletedEvent(this, project.getId()));
    }

    @Override
    @Transactional
    public void archiveResearch(UUID projectId) {
        ResearchProject project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));
        
        project.setStatus(ResearchStatus.ARCHIVED);
        projectRepository.save(project);

        eventPublisher.publishEvent(new ResearchStatusChangedEvent(this, project.getId(), ResearchStatus.ARCHIVED));
        eventPublisher.publishEvent(new ResearchArchivedEvent(this, project.getId()));
    }

    @Override
    @Transactional
    public void restoreResearch(UUID projectId) {
        ResearchProject project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));
        
        project.setDeletedAt(null);
        projectRepository.save(project);
        
        eventPublisher.publishEvent(new ResearchUpdatedEvent(this, project.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchDetailResponse getResearch(UUID projectId) {
        ResearchProject project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));
        
        return buildDetailResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ResearchDetailResponse getResearchBySlug(String slug) {
        ResearchProject project = projectRepository.findBySlugAndDeletedAtIsNull(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));

        return buildDetailResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResearchSummaryResponse> searchResearch(String title, String category, String status, String keyword, Pageable pageable) {
        Page<ResearchProject> projects = projectRepository.findAll(
                ResearchSpecification.filterBy(title, status, keyword, null, null), pageable);

        return projects.map(p -> ResearchSummaryResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .slug(p.getSlug())
                .abstractText(p.getAbstractText())
                .categoryName(p.getCategory().getName())
                .status(p.getStatus().name())
                .researchType(p.getResearchType().name())
                .publishedDate(p.getPublishedDate())
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResearchMemberResponse> getMembers(UUID projectId) {
        return authorRepository.findByProjectIdAndDeletedAtIsNull(projectId).stream()
                .map(a -> ResearchMemberResponse.builder()
                        .userId(a.getUser().getId())
                        .firstName(a.getUser().getFirstName())
                        .lastName(a.getUser().getLastName())
                        .isPrimary(a.getIsPrimary())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addMember(UUID projectId, AddMemberRequest request) {
        ResearchProject project = projectRepository.findByIdAndDeletedAtIsNull(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Research project not found"));
        
        if (authorRepository.existsByProjectIdAndUserIdAndDeletedAtIsNull(projectId, request.getUserId())) {
            throw new BusinessException("User is already a member of this research project");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        ResearchAuthor author = ResearchAuthor.builder()
                .project(project)
                .user(user)
                .isPrimary(request.isPrimary())
                .build();
        authorRepository.save(author);

        eventPublisher.publishEvent(new ResearchMemberAddedEvent(this, project.getId(), user.getId()));
    }

    @Override
    @Transactional
    public void removeMember(UUID projectId, UUID memberId) {
        ResearchAuthor author = authorRepository.findByProjectIdAndUserIdAndDeletedAtIsNull(projectId, memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in this project"));
        
        author.setDeletedAt(LocalDateTime.now());
        authorRepository.save(author);

        eventPublisher.publishEvent(new ResearchMemberRemovedEvent(this, projectId, memberId));
    }

    private void syncKeywords(ResearchProject project, List<String> keywords) {
        if (keywords == null) return;
        
        // Remove old keyword mappings softly (or fully since map table has cascade delete and no deletedAt used generally)
        // the map table has no deletedAt in script, so we hard delete
        projectKeywordRepository.findByProjectId(project.getId()).forEach(km -> {
            projectKeywordRepository.deleteByProjectIdAndKeywordId(project.getId(), km.getKeyword().getId());
        });

        // Add new
        for (String kw : keywords) {
            String sanitizedKw = kw.trim().toLowerCase();
            ResearchKeyword keyword = keywordRepository.findByKeywordIgnoreCaseAndDeletedAtIsNull(sanitizedKw)
                    .orElseGet(() -> {
                        ResearchKeyword newKw = ResearchKeyword.builder().keyword(sanitizedKw).build();
                        return keywordRepository.save(newKw);
                    });

            ResearchProjectKeyword map = ResearchProjectKeyword.builder()
                    .project(project)
                    .keyword(keyword)
                    .build();
            projectKeywordRepository.save(map);
        }
    }

    private ResearchDetailResponse buildDetailResponse(ResearchProject project) {
        List<ResearchMemberResponse> members = getMembers(project.getId());
        List<String> keywords = projectKeywordRepository.findByProjectId(project.getId()).stream()
                .map(km -> km.getKeyword().getKeyword())
                .collect(Collectors.toList());

        return ResearchDetailResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .slug(project.getSlug())
                .abstractText(project.getAbstractText())
                .content(project.getContent())
                .categoryName(project.getCategory().getName())
                .status(project.getStatus().name())
                .researchType(project.getResearchType().name())
                .coverMediaId(project.getCoverMediaId())
                .publishedDate(project.getPublishedDate())
                .keywords(keywords)
                .members(members)
                .build();
    }
}
