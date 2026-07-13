package com.hssa.media.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface StorageService {
    
    /**
     * Store the file securely and return the relative stored path.
     */
    String store(MultipartFile file, String storedName);
    
    /**
     * Load the file as a resource.
     */
    Resource loadAsResource(String storedName);
    
    /**
     * Delete the file from storage.
     */
    void delete(String storedName);
    
    /**
     * Generate a signed URL for the file (not yet implemented).
     */
    String generateSignedUrl(String storedName);
}
