package com.hssa.media.service.impl;

import com.hssa.auth.entity.User;
import com.hssa.auth.repository.UserRepository;
import com.hssa.exception.BusinessException;
import com.hssa.exception.ResourceNotFoundException;
import com.hssa.media.config.MediaProperties;
import com.hssa.media.dto.request.SearchMediaRequest;
import com.hssa.media.dto.request.UpdateMetadataRequest;
import com.hssa.media.dto.request.UploadRequest;
import com.hssa.media.dto.response.FileMetadataResponse;
import com.hssa.media.dto.response.MediaResponse;
import com.hssa.media.dto.response.MediaSummaryResponse;
import com.hssa.media.entity.MediaDownload;
import com.hssa.media.entity.MediaFile;
import com.hssa.media.enums.MediaType;
import com.hssa.media.enums.StorageProvider;
import com.hssa.media.event.FileDeletedEvent;
import com.hssa.media.event.FileDownloadedEvent;
import com.hssa.media.event.FileUploadedEvent;
import com.hssa.media.event.VisibilityChangedEvent;
import com.hssa.media.repository.MediaDownloadRepository;
import com.hssa.media.repository.MediaFileRepository;
import com.hssa.media.service.MediaService;
import com.hssa.media.specification.MediaSpecification;
import com.hssa.media.storage.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaFileRepository mediaFileRepository;
    private final MediaDownloadRepository mediaDownloadRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;
    private final MediaProperties mediaProperties;
    private final ApplicationEventPublisher eventPublisher;

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User not found"));
    }

    private MediaFile getMediaFile(UUID id) {
        return mediaFileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found with ID: " + id));
    }
    
    private MediaFile getActiveMediaFile(UUID id) {
        return mediaFileRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Media file not found or is deleted with ID: " + id));
    }

    private void validateAccess(MediaFile file, UUID userId, boolean isAdmin) {
        if (!file.getIsPublic()) {
            if (userId == null) {
                throw new BusinessException("Unauthorized access to private file");
            }
            if (!isAdmin && !file.getUploadedBy().getId().equals(userId)) {
                throw new BusinessException("You do not have permission to access this file");
            }
        }
    }

    private void validateModificationAccess(MediaFile file, UUID userId, boolean isAdmin) {
        if (!isAdmin && !file.getUploadedBy().getId().equals(userId)) {
            throw new BusinessException("You do not have permission to modify this file");
        }
    }

    @Override
    @Transactional
    public MediaResponse upload(UploadRequest request, UUID userId) {
        MultipartFile file = request.getFile();
        if (file.isEmpty()) {
            throw new BusinessException("File is empty");
        }
        
        if (file.getSize() > mediaProperties.getValidation().getMaxFileSize()) {
            throw new BusinessException("File size exceeds maximum allowed size");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) originalFilename = "unknown";
        
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }
        
        if (!mediaProperties.getValidation().getAllowedExtensions().contains(extension)) {
            throw new BusinessException("File extension not allowed: " + extension);
        }

        User uploader = getUser(userId);
        
        String storedName = UUID.randomUUID().toString() + "." + extension;
        String filePath = storageService.store(file, storedName);
        
        MediaType type = determineMediaType(extension);
        
        MediaFile mediaFile = MediaFile.builder()
                .uploadedBy(uploader)
                .folderId(request.getFolderId())
                .mediaType(type)
                .storageProvider(StorageProvider.valueOf(mediaProperties.getStorage().getDefaultProvider()))
                .originalName(originalFilename)
                .storedName(storedName)
                .fileExtension(extension)
                .mimeType(file.getContentType())
                .fileSize(file.getSize())
                // Replaced with the public UUID endpoint immediately after the first insert.
                .fileUrl("/api/v1/media/download/pending")
                .altText(request.getAltText())
                .caption(request.getCaption())
                .isPublic(request.getIsPublic())
                .build();
                
        mediaFile = mediaFileRepository.save(mediaFile);
        mediaFile.setFileUrl("/api/v1/media/download/" + mediaFile.getId());
        mediaFile = mediaFileRepository.save(mediaFile);
        
        log.info("Uploaded file {} by user {}", mediaFile.getId(), userId);
        eventPublisher.publishEvent(new FileUploadedEvent(this, mediaFile.getId()));
        
        return toMediaResponse(mediaFile);
    }

    private MediaType determineMediaType(String extension) {
        switch (extension) {
            case "jpg": case "jpeg": case "png": case "gif": case "webp": return MediaType.IMAGE;
            case "mp4": case "webm": case "avi": case "mov": return MediaType.VIDEO;
            case "mp3": case "wav": case "ogg": return MediaType.AUDIO;
            case "pdf": return MediaType.PDF;
            case "doc": case "docx": case "ppt": case "pptx": case "xls": case "xlsx": case "txt": case "csv": return MediaType.DOCUMENT;
            case "zip": case "rar": return MediaType.ARCHIVE;
            default: return MediaType.OTHER;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FileMetadataResponse getMetadata(UUID id, UUID userId, boolean isAdmin) {
        MediaFile file = getActiveMediaFile(id);
        validateAccess(file, userId, isAdmin);
        
        return FileMetadataResponse.builder()
                .id(file.getId())
                .originalName(file.getOriginalName())
                .storedName(file.getStoredName())
                .extension(file.getFileExtension())
                .mimeType(file.getMimeType())
                .size(file.getFileSize())
                .checksum(file.getChecksum())
                .ownerId(file.getUploadedBy().getId())
                .uploadTime(file.getCreatedAt())
                .isPublic(file.getIsPublic())
                .downloadCount(file.getDownloadCount())
                .build();
    }

    @Override
    @Transactional
    public Resource download(UUID id, UUID userId, boolean isAdmin, String ipAddress) {
        MediaFile file = getActiveMediaFile(id);
        validateAccess(file, userId, isAdmin);
        
        file.setDownloadCount(file.getDownloadCount() + 1);
        mediaFileRepository.save(file);
        
        MediaDownload download = MediaDownload.builder()
                .mediaFile(file)
                .user(userId != null ? getUser(userId) : null)
                .ipAddress(ipAddress)
                .build();
        mediaDownloadRepository.save(download);
        
        eventPublisher.publishEvent(new FileDownloadedEvent(this, file.getId()));
        
        return storageService.loadAsResource(file.getStoredName());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MediaSummaryResponse> search(SearchMediaRequest request, Pageable pageable) {
        return mediaFileRepository.findAll(MediaSpecification.filterBy(request), pageable)
                .map(file -> MediaSummaryResponse.builder()
                        .id(file.getId())
                        .originalName(file.getOriginalName())
                        .mediaType(file.getMediaType().name())
                        .fileUrl(file.getFileUrl())
                        .thumbnailUrl(file.getThumbnailUrl())
                        .fileSize(file.getFileSize())
                        .isPublic(file.getIsPublic())
                        .createdAt(file.getCreatedAt())
                        .build());
    }

    @Override
    @Transactional
    public FileMetadataResponse updateMetadata(UUID id, UpdateMetadataRequest request, UUID userId, boolean isAdmin) {
        MediaFile file = getActiveMediaFile(id);
        validateModificationAccess(file, userId, isAdmin);
        
        if (request.getAltText() != null) file.setAltText(request.getAltText());
        if (request.getCaption() != null) file.setCaption(request.getCaption());
        
        mediaFileRepository.save(file);
        return getMetadata(id, userId, isAdmin);
    }

    @Override
    @Transactional
    public void delete(UUID id, UUID userId, boolean isAdmin) {
        MediaFile file = getActiveMediaFile(id);
        validateModificationAccess(file, userId, isAdmin);
        
        file.setDeletedAt(LocalDateTime.now());
        mediaFileRepository.save(file);
        
        log.info("Soft deleted file {}", id);
        eventPublisher.publishEvent(new FileDeletedEvent(this, id));
    }

    @Override
    @Transactional
    public void restore(UUID id, UUID userId, boolean isAdmin) {
        MediaFile file = getMediaFile(id);
        validateModificationAccess(file, userId, isAdmin);
        
        if (file.getDeletedAt() != null) {
            file.setDeletedAt(null);
            mediaFileRepository.save(file);
            log.info("Restored file {}", id);
        }
    }

    @Override
    @Transactional
    public void updateVisibility(UUID id, boolean isPublic, UUID userId, boolean isAdmin) {
        MediaFile file = getActiveMediaFile(id);
        validateModificationAccess(file, userId, isAdmin);
        
        file.setIsPublic(isPublic);
        mediaFileRepository.save(file);
        
        log.info("Updated visibility for file {} to {}", id, isPublic);
        eventPublisher.publishEvent(new VisibilityChangedEvent(this, id));
    }
    
    private MediaResponse toMediaResponse(MediaFile file) {
        return MediaResponse.builder()
                .id(file.getId())
                .folderId(file.getFolderId())
                .uploadedBy(file.getUploadedBy().getId())
                .mediaType(file.getMediaType().name())
                .storageProvider(file.getStorageProvider().name())
                .originalName(file.getOriginalName())
                .fileExtension(file.getFileExtension())
                .mimeType(file.getMimeType())
                .fileSize(file.getFileSize())
                .fileUrl(file.getFileUrl())
                .thumbnailUrl(file.getThumbnailUrl())
                .width(file.getWidth())
                .height(file.getHeight())
                .durationSeconds(file.getDurationSeconds())
                .checksum(file.getChecksum())
                .altText(file.getAltText())
                .caption(file.getCaption())
                .isPublic(file.getIsPublic())
                .downloadCount(file.getDownloadCount())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
    }
}
