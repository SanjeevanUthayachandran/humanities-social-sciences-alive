package com.hssa.course.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignInstructorByEmailRequest {
    @Email
    @NotBlank
    private String email;
    @NotNull
    private UUID courseId;
    private boolean primary;
}
