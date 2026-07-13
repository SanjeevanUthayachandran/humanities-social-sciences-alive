package com.hssa.literary.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentRequest {
    @NotBlank(message = "Comment cannot be empty")
    private String comment;
    private UUID parentCommentId;
}
