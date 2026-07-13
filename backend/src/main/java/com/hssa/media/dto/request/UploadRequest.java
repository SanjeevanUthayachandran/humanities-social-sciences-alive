package com.hssa.media.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class UploadRequest {
    @NotNull(message = "File cannot be null")
    private MultipartFile file;
    
    private UUID folderId;
    
    private Boolean isPublic = false;
    
    @Size(max = 255)
    private String altText;
    
    private String caption;
}
