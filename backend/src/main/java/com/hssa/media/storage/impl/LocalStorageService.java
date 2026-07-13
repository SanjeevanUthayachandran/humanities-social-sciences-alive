package com.hssa.media.storage.impl;

import com.hssa.exception.BusinessException;
import com.hssa.media.config.MediaProperties;
import com.hssa.media.storage.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class LocalStorageService implements StorageService {

    private final Path rootLocation;

    public LocalStorageService(MediaProperties properties) {
        this.rootLocation = Paths.get(properties.getStorage().getLocal().getBasePath());
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new BusinessException("Could not initialize storage location");
        }
    }

    @Override
    public String store(MultipartFile file, String storedName) {
        try {
            if (file.isEmpty()) {
                throw new BusinessException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new BusinessException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return storedName;
        } catch (IOException e) {
            throw new BusinessException("Failed to store file.");
        }
    }

    @Override
    public Resource loadAsResource(String storedName) {
        try {
            Path file = rootLocation.resolve(storedName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new BusinessException("Could not read file: " + storedName);
            }
        } catch (MalformedURLException e) {
            throw new BusinessException("Could not read file: " + storedName);
        }
    }

    @Override
    public void delete(String storedName) {
        try {
            Path file = rootLocation.resolve(storedName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", storedName, e);
        }
    }

    @Override
    public String generateSignedUrl(String storedName) {
        throw new UnsupportedOperationException("Signed URLs are not supported in local storage.");
    }
}
