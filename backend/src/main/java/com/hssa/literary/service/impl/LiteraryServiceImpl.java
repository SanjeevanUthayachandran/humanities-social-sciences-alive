package com.hssa.literary.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.literary.dto.request.CreateLiteraryWorkRequest;
import com.hssa.literary.dto.request.UpdateLiteraryWorkRequest;
import com.hssa.literary.dto.response.LiteraryCategoryResponse;
import com.hssa.literary.dto.response.LiteraryWorkDetailResponse;
import com.hssa.literary.dto.response.LiteraryWorkSummaryResponse;
import com.hssa.literary.entity.*;
import com.hssa.literary.enums.ContentStatus;
import com.hssa.literary.event.*;
import com.hssa.literary.repository.*;
import com.hssa.literary.service.LiteraryService;
import com.hssa.literary.specification.LiterarySpecification;
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
public class LiteraryServiceImpl implements LiteraryService {

    private final LiteraryArticleRepository articleRepository;
    private final LiteraryCategoryRepository categoryRepository;
    private final LiteraryTagRepository tagRepository;
    private final LiteraryArticleTagRepository articleTagRepository;
    private final LiteraryLikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public LiteraryWorkDetailResponse createWork(CreateLiteraryWorkRequest request, UUID authorId) {
        LiteraryCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String slug = request.getTitle().toLowerCase().replaceAll("[^a-z0-9]+", "-") + "-" + System.currentTimeMillis();

        LiteraryArticle article = LiteraryArticle.builder()
                .category(category)
                .author(author)
                .articleType(request.getArticleType())
                .title(request.getTitle())
                .slug(slug)
                .summary(request.getSummary())
                .content(request.getContent())
                .featuredImage(request.getFeaturedImage())
                .language(request.getLanguage() != null ? request.getLanguage() : "EN")
                .readingTime(request.getReadingTime())
                .allowComments(request.getAllowComments() != null ? request.getAllowComments() : true)
                .status(ContentStatus.DRAFT)
                .build();

        article = articleRepository.save(article);
        syncTags(article, request.getTags());

        eventPublisher.publishEvent(new LiteraryWorkCreatedEvent(this, article.getId()));
        return buildDetail(article);
    }

    @Override
    @Transactional
    public LiteraryWorkDetailResponse updateWork(UUID articleId, UpdateLiteraryWorkRequest request) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        if (request.getTitle() != null) article.setTitle(request.getTitle());
        if (request.getContent() != null) article.setContent(request.getContent());
        if (request.getSummary() != null) article.setSummary(request.getSummary());
        if (request.getArticleType() != null) article.setArticleType(request.getArticleType());
        if (request.getFeaturedImage() != null) article.setFeaturedImage(request.getFeaturedImage());
        if (request.getLanguage() != null) article.setLanguage(request.getLanguage());
        if (request.getReadingTime() != null) article.setReadingTime(request.getReadingTime());
        if (request.getAllowComments() != null) article.setAllowComments(request.getAllowComments());
        if (request.getIsFeatured() != null) article.setIsFeatured(request.getIsFeatured());
        if (request.getCategoryId() != null) {
            LiteraryCategory cat = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(cat);
        }
        if (request.getTags() != null) syncTags(article, request.getTags());

        article = articleRepository.save(article);
        eventPublisher.publishEvent(new LiteraryWorkUpdatedEvent(this, article.getId()));
        return buildDetail(article);
    }

    @Override
    @Transactional
    public void deleteWork(UUID articleId) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setDeletedAt(LocalDateTime.now());
        articleRepository.save(article);
    }

    @Override
    @Transactional
    public void publishWork(UUID articleId) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        if (article.getStatus() == ContentStatus.ARCHIVED) {
            throw new BusinessException("Cannot publish an archived article");
        }
        article.setStatus(ContentStatus.PUBLISHED);
        article.setPublishedAt(LocalDateTime.now());
        articleRepository.save(article);
        eventPublisher.publishEvent(new LiteraryWorkPublishedEvent(this, article.getId()));
    }

    @Override
    @Transactional
    public void archiveWork(UUID articleId) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setStatus(ContentStatus.ARCHIVED);
        articleRepository.save(article);
        eventPublisher.publishEvent(new LiteraryWorkArchivedEvent(this, article.getId()));
    }

    @Override
    @Transactional
    public void restoreWork(UUID articleId) {
        LiteraryArticle article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setDeletedAt(null);
        articleRepository.save(article);
    }

    @Override
    @Transactional(readOnly = true)
    public LiteraryWorkDetailResponse getWork(UUID articleId) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        return buildDetail(article);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LiteraryWorkSummaryResponse> search(String title, String status, String articleType, UUID categoryId, UUID authorId, Boolean isFeatured, Pageable pageable) {
        return articleRepository.findAll(
                LiterarySpecification.filterBy(title, status, articleType, categoryId, authorId, isFeatured), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LiteraryWorkSummaryResponse> getFeatured(Pageable pageable) {
        return articleRepository.findAll(
                LiterarySpecification.filterBy(null, "PUBLISHED", null, null, null, true), pageable)
                .map(this::buildSummary);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiteraryCategoryResponse> listCategories() {
        return categoryRepository.findAll().stream()
                .map(c -> LiteraryCategoryResponse.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .description(c.getDescription())
                        .isActive(c.getIsActive())
                        .parentId(c.getParent() != null ? c.getParent().getId() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private void syncTags(LiteraryArticle article, List<String> tags) {
        if (tags == null) return;
        articleTagRepository.deleteByArticleId(article.getId());
        for (String tagName : tags) {
            String sanitized = tagName.trim().toLowerCase();
            LiteraryTag tag = tagRepository.findByNameIgnoreCase(sanitized)
                    .orElseGet(() -> tagRepository.save(LiteraryTag.builder()
                            .name(sanitized)
                            .slug(sanitized.replaceAll("[^a-z0-9]+", "-"))
                            .build()));
            LiteraryArticleTagId id = new LiteraryArticleTagId(article.getId(), tag.getId());
            articleTagRepository.save(LiteraryArticleTag.builder().id(id).article(article).tag(tag).build());
        }
    }

    private List<String> getTagsForArticle(UUID articleId) {
        return articleTagRepository.findByArticleId(articleId).stream()
                .map(at -> at.getTag().getName())
                .collect(Collectors.toList());
    }

    private LiteraryWorkDetailResponse buildDetail(LiteraryArticle a) {
        return LiteraryWorkDetailResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .slug(a.getSlug())
                .articleType(a.getArticleType().name())
                .status(a.getStatus().name())
                .categoryName(a.getCategory().getName())
                .authorId(a.getAuthor().getId())
                .authorFullName(a.getAuthor().getFirstName() + " " + a.getAuthor().getLastName())
                .summary(a.getSummary())
                .content(a.getContent())
                .featuredImage(a.getFeaturedImage())
                .language(a.getLanguage())
                .readingTime(a.getReadingTime())
                .isFeatured(a.getIsFeatured())
                .allowComments(a.getAllowComments())
                .viewCount(a.getViewCount())
                .publishedAt(a.getPublishedAt())
                .createdAt(a.getCreatedAt())
                .tags(getTagsForArticle(a.getId()))
                .likeCount(likeRepository.countByArticleId(a.getId()))
                .build();
    }

    private LiteraryWorkSummaryResponse buildSummary(LiteraryArticle a) {
        return LiteraryWorkSummaryResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .slug(a.getSlug())
                .articleType(a.getArticleType().name())
                .status(a.getStatus().name())
                .categoryName(a.getCategory().getName())
                .authorFullName(a.getAuthor().getFirstName() + " " + a.getAuthor().getLastName())
                .isFeatured(a.getIsFeatured())
                .viewCount(a.getViewCount())
                .publishedAt(a.getPublishedAt())
                .tags(getTagsForArticle(a.getId()))
                .build();
    }
}
