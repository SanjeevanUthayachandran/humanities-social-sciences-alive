package com.hssa.media.controller;

import com.hssa.security.dto.CustomUserDetails;
import com.hssa.common.dto.pagination.PaginationResponse;
import com.hssa.common.response.ApiResponse;
import com.hssa.media.dto.request.SearchMediaRequest;
import com.hssa.media.dto.request.UpdateMetadataRequest;
import com.hssa.media.dto.request.UploadRequest;
import com.hssa.media.dto.response.FileMetadataResponse;
import com.hssa.media.dto.response.MediaResponse;
import com.hssa.media.dto.response.MediaSummaryResponse;
import com.hssa.media.service.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Tag(name = "Media Management", description = "Endpoints for handling file uploads, downloads, and metadata")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Upload a file", description = "Upload a media file and store its metadata")
    public ResponseEntity<ApiResponse<MediaResponse>> uploadFile(
            @Valid @ModelAttribute UploadRequest request,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
        
        MediaResponse response = mediaService.upload(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success(response, "File uploaded successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get file metadata", description = "Retrieve metadata of a specific file")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> getMetadata(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
        
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        FileMetadataResponse response = mediaService.getMetadata(id, userPrincipal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Metadata retrieved successfully"));
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Download a file", description = "Download the raw file content")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable UUID id,
            HttpServletRequest request,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
            
        UUID userId = userPrincipal != null ? userPrincipal.getId() : null;
        boolean isAdmin = userPrincipal != null && userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        String ipAddress = request.getRemoteAddr();
        
        Resource resource = mediaService.download(id, userId, isAdmin, ipAddress);
        
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            // Fallback to default
        }
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Search files", description = "Search and filter media files dynamically")
    public ResponseEntity<ApiResponse<PaginationResponse<MediaSummaryResponse>>> searchFiles(
            @RequestBody SearchMediaRequest request,
            Pageable pageable) {
        
        Page<MediaSummaryResponse> page = mediaService.search(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(PaginationResponse.fromPage(page), "Files searched successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update metadata", description = "Update caption or alt text of a file")
    public ResponseEntity<ApiResponse<FileMetadataResponse>> updateMetadata(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMetadataRequest request,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
            
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        FileMetadataResponse response = mediaService.updateMetadata(id, request, userPrincipal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(response, "Metadata updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Soft delete a file", description = "Soft deletes a media file")
    public ResponseEntity<ApiResponse<Void>> deleteFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
            
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        mediaService.delete(id, userPrincipal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "File soft deleted successfully"));
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Restore a file", description = "Restores a soft deleted file")
    public ResponseEntity<ApiResponse<Void>> restoreFile(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
            
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        mediaService.restore(id, userPrincipal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "File restored successfully"));
    }

    @PatchMapping("/{id}/visibility")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Change visibility", description = "Updates whether a file is public or private")
    public ResponseEntity<ApiResponse<Void>> updateVisibility(
            @PathVariable UUID id,
            @RequestParam boolean isPublic,
            @AuthenticationPrincipal CustomUserDetails userPrincipal) {
            
        boolean isAdmin = userPrincipal.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                
        mediaService.updateVisibility(id, isPublic, userPrincipal.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success(null, "File visibility updated successfully"));
    }
}
