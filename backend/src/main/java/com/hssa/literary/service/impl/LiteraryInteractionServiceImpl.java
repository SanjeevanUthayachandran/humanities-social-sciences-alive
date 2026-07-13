package com.hssa.literary.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.literary.dto.request.CommentRequest;
import com.hssa.literary.dto.response.LiteraryCommentResponse;
import com.hssa.literary.entity.*;
import com.hssa.literary.event.*;
import com.hssa.literary.repository.*;
import com.hssa.literary.service.LiteraryInteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiteraryInteractionServiceImpl implements LiteraryInteractionService {

    private final LiteraryArticleRepository articleRepository;
    private final LiteraryLikeRepository likeRepository;
    private final LiteraryBookmarkRepository bookmarkRepository;
    private final LiteraryCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public void likeArticle(UUID articleId, UUID userId) {
        if (likeRepository.existsByArticleIdAndUserId(articleId, userId)) {
            throw new BusinessException("Article already liked");
        }
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        likeRepository.save(LiteraryLike.builder().article(article).user(user).build());
        eventPublisher.publishEvent(new LikeAddedEvent(this, articleId, userId));
    }

    @Override
    @Transactional
    public void unlikeArticle(UUID articleId, UUID userId) {
        LiteraryLike like = likeRepository.findByArticleIdAndUserId(articleId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Like not found"));
        likeRepository.delete(like);
    }

    @Override
    @Transactional
    public void bookmarkArticle(UUID articleId, UUID userId) {
        if (bookmarkRepository.existsByArticleIdAndUserId(articleId, userId)) {
            throw new BusinessException("Article already bookmarked");
        }
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        bookmarkRepository.save(LiteraryBookmark.builder().article(article).user(user).build());
        eventPublisher.publishEvent(new FavoriteAddedEvent(this, articleId, userId));
    }

    @Override
    @Transactional
    public void unbookmarkArticle(UUID articleId, UUID userId) {
        LiteraryBookmark bookmark = bookmarkRepository.findByArticleIdAndUserId(articleId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Bookmark not found"));
        bookmarkRepository.delete(bookmark);
    }

    @Override
    @Transactional
    public LiteraryCommentResponse addComment(UUID articleId, CommentRequest request, UUID userId) {
        LiteraryArticle article = articleRepository.findByIdAndDeletedAtIsNull(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        LiteraryComment parent = null;
        if (request.getParentCommentId() != null) {
            parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
        }

        LiteraryComment comment = LiteraryComment.builder()
                .article(article)
                .user(user)
                .parentComment(parent)
                .comment(request.getComment())
                .isApproved(true)
                .build();
        comment = commentRepository.save(comment);
        eventPublisher.publishEvent(new CommentCreatedEvent(this, articleId, comment.getId()));
        return toCommentResponse(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LiteraryCommentResponse> getComments(UUID articleId) {
        return commentRepository.findByArticleIdAndParentCommentIsNullOrderByCreatedAtAsc(articleId).stream()
                .map(c -> {
                    LiteraryCommentResponse res = toCommentResponse(c);
                    List<LiteraryCommentResponse> replies = commentRepository.findByParentCommentId(c.getId()).stream()
                            .map(this::toCommentResponse)
                            .collect(Collectors.toList());
                    res.setReplies(replies);
                    return res;
                })
                .collect(Collectors.toList());
    }

    private LiteraryCommentResponse toCommentResponse(LiteraryComment c) {
        return LiteraryCommentResponse.builder()
                .id(c.getId())
                .userId(c.getUser().getId())
                .userFullName(c.getUser().getFirstName() + " " + c.getUser().getLastName())
                .comment(c.getComment())
                .isApproved(c.getIsApproved())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
