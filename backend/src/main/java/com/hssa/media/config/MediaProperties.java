package com.hssa.media.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "hssa.media")
public class MediaProperties {

    private Storage storage = new Storage();
    private Validation validation = new Validation();

    @Data
    public static class Storage {
        private String defaultProvider = "LOCAL";
        private Local local = new Local();
    }

    @Data
    public static class Local {
        private String basePath = "uploads/media";
    }

    @Data
    public static class Validation {
        private long maxFileSize = 52428800; // 50MB default
        private List<String> allowedExtensions = List.of(
                "jpg", "jpeg", "png", "gif", "webp",
                "pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "csv",
                "zip", "rar",
                "mp4", "webm", "avi", "mov",
                "mp3", "wav", "ogg"
        );
    }
}
