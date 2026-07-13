package com.hssa.literary.service;

import com.hssa.literary.dto.request.CommentRequest;
import com.hssa.literary.dto.response.LiteraryCommentResponse;

import java.util.List;
import java.util.UUID;

public interface LiteraryInteractionService {
    void likeArticle(UUID articleId, UUID userId);
    void unlikeArticle(UUID articleId, UUID userId);
    void bookmarkArticle(UUID articleId, UUID userId);
    void unbookmarkArticle(UUID articleId, UUID userId);
    LiteraryCommentResponse addComment(UUID articleId, CommentRequest request, UUID userId);
    List<LiteraryCommentResponse> getComments(UUID articleId);
}
