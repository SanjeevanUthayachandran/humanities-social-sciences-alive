package com.hssa.workshop.service;

import com.hssa.workshop.dto.request.CreateWorkshopRequest;
import com.hssa.workshop.dto.request.UpdateWorkshopRequest;
import com.hssa.workshop.dto.response.WorkshopDetailResponse;
import com.hssa.workshop.dto.response.WorkshopStatisticsResponse;
import com.hssa.workshop.dto.response.WorkshopSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface WorkshopService {
    WorkshopDetailResponse createWorkshop(CreateWorkshopRequest request, UUID creatorId);
    WorkshopDetailResponse updateWorkshop(UUID id, UpdateWorkshopRequest request, UUID updaterId);
    void deleteWorkshop(UUID id);
    void restoreWorkshop(UUID id);
    void publishWorkshop(UUID id);
    void cancelWorkshop(UUID id);
    void completeWorkshop(UUID id);
    WorkshopDetailResponse getWorkshop(UUID id);
    Page<WorkshopSummaryResponse> listWorkshops(Pageable pageable);
    Page<WorkshopSummaryResponse> searchWorkshops(String title, String status, String eventType, String locationType, Boolean isFree, Pageable pageable);
    Page<WorkshopSummaryResponse> getFeatured(Pageable pageable);
    WorkshopStatisticsResponse getStatistics();
}
