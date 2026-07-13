package com.hssa.literary.service;

import com.hssa.literary.dto.request.CreateLiteraryWorkRequest;
import com.hssa.literary.dto.request.UpdateLiteraryWorkRequest;
import com.hssa.literary.dto.response.LiteraryCategoryResponse;
import com.hssa.literary.dto.response.LiteraryWorkDetailResponse;
import com.hssa.literary.dto.response.LiteraryWorkSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface LiteraryService {
    LiteraryWorkDetailResponse createWork(CreateLiteraryWorkRequest request, UUID authorId);
    LiteraryWorkDetailResponse updateWork(UUID articleId, UpdateLiteraryWorkRequest request);
    void deleteWork(UUID articleId);
    void publishWork(UUID articleId);
    void archiveWork(UUID articleId);
    void restoreWork(UUID articleId);
    LiteraryWorkDetailResponse getWork(UUID articleId);
    Page<LiteraryWorkSummaryResponse> search(String title, String status, String articleType, UUID categoryId, UUID authorId, Boolean isFeatured, Pageable pageable);
    Page<LiteraryWorkSummaryResponse> getFeatured(Pageable pageable);
    List<LiteraryCategoryResponse> listCategories();
}
