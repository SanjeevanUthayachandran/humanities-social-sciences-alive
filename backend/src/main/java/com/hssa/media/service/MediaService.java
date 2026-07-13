package com.hssa.media.service;

import com.hssa.media.dto.request.SearchMediaRequest;
import com.hssa.media.dto.request.UpdateMetadataRequest;
import com.hssa.media.dto.request.UploadRequest;
import com.hssa.media.dto.response.FileMetadataResponse;
import com.hssa.media.dto.response.MediaResponse;
import com.hssa.media.dto.response.MediaSummaryResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MediaService {

    MediaResponse upload(UploadRequest request, UUID userId);
    
    FileMetadataResponse getMetadata(UUID id, UUID userId, boolean isAdmin);
    
    Resource download(UUID id, UUID userId, boolean isAdmin, String ipAddress);
    
    Page<MediaSummaryResponse> search(SearchMediaRequest request, Pageable pageable);
    
    FileMetadataResponse updateMetadata(UUID id, UpdateMetadataRequest request, UUID userId, boolean isAdmin);
    
    void delete(UUID id, UUID userId, boolean isAdmin);
    
    void restore(UUID id, UUID userId, boolean isAdmin);
    
    void updateVisibility(UUID id, boolean isPublic, UUID userId, boolean isAdmin);
}
