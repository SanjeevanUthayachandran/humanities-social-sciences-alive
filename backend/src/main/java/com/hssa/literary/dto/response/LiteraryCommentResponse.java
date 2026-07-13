package com.hssa.literary.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LiteraryCommentResponse {
    private UUID id;
    private UUID userId;
    private String userFullName;
    private String comment;
    private Boolean isApproved;
    private LocalDateTime createdAt;
    private List<LiteraryCommentResponse> replies;
}
