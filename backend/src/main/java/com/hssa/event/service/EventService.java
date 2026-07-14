package com.hssa.event.service;

import com.hssa.event.dto.request.CreateEventRequest;
import com.hssa.event.dto.request.UpdateEventRequest;
import com.hssa.event.dto.response.EventDetailResponse;
import com.hssa.event.dto.response.EventStatisticsResponse;
import com.hssa.event.dto.response.EventSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EventService {
    EventDetailResponse createEvent(CreateEventRequest request, UUID creatorId);
    EventDetailResponse updateEvent(UUID id, UpdateEventRequest request, UUID updaterId);
    void deleteEvent(UUID id);
    void restoreEvent(UUID id);
    void publishEvent(UUID id);
    void cancelEvent(UUID id);
    void completeEvent(UUID id);
    EventDetailResponse getEvent(UUID id);
    Page<EventSummaryResponse> listEvents(Pageable pageable);
    Page<EventSummaryResponse> searchEvents(String title, String status, String eventType, String locationType, Boolean isFree, Pageable pageable);
    Page<EventSummaryResponse> getUpcoming(Pageable pageable);
    Page<EventSummaryResponse> getFeatured(Pageable pageable);
    EventStatisticsResponse getStatistics();
}