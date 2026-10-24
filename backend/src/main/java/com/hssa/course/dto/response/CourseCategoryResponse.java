package com.hssa.course.dto.response;

import java.util.UUID;

public record CourseCategoryResponse(UUID id, String name, String slug, String description) {
}
