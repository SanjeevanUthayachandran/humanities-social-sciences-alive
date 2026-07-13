package com.hssa.media.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateMetadataRequest {
    @Size(max = 255)
    private String altText;
    
    private String caption;
}
