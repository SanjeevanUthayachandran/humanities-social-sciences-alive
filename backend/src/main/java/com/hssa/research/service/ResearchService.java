package com.hssa.research.service;

import com.hssa.research.dto.request.CreateResearchRequest;
import com.hssa.research.dto.request.UpdateResearchRequest;
import com.hssa.research.dto.request.AddMemberRequest;
import com.hssa.research.dto.response.ResearchDetailResponse;
import com.hssa.research.dto.response.ResearchMemberResponse;
import com.hssa.research.dto.response.ResearchSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ResearchService {
    ResearchDetailResponse createResearch(CreateResearchRequest request, UUID creatorId);
    ResearchDetailResponse updateResearch(UUID projectId, UpdateResearchRequest request);
    void deleteResearch(UUID projectId);
    void archiveResearch(UUID projectId);
    void restoreResearch(UUID projectId);
    ResearchDetailResponse getResearch(UUID projectId);
    ResearchDetailResponse getResearchBySlug(String slug);
    Page<ResearchSummaryResponse> searchResearch(String title, String category, String status, String keyword, Pageable pageable);
    
    // Member Management
    List<ResearchMemberResponse> getMembers(UUID projectId);
    void addMember(UUID projectId, AddMemberRequest request);
    void removeMember(UUID projectId, UUID memberId);
}
